package com.hotel.service;

import com.hotel.model.AdminUser;
import com.hotel.model.FlexibleReservation;
import com.hotel.model.NonRefundableReservation;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.model.StandardReservation;
import com.hotel.model.User;
import com.hotel.util.FileHandler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private static final String FILE_NAME = "reservations.txt";

    private RoomService roomService = new RoomService();

    // Builds the right subclass for a booking type (polymorphism: all are Reservations)
    public static Reservation createReservation(String id, String userId, String roomId,
                                                LocalDate in, LocalDate out, String type,
                                                double total, String status) {
        switch (type) {
            case "FLEXIBLE":
                return new FlexibleReservation(id, userId, roomId, in, out, total, status);
            case "NON_REFUNDABLE":
                return new NonRefundableReservation(id, userId, roomId, in, out, total, status);
            default:
                return new StandardReservation(id, userId, roomId, in, out, total, status);
        }
    }

    // Price = room price for the nights (Room polymorphism) x booking type factor
    public double calculatePrice(Room room, String type, LocalDate in, LocalDate out) {
        int nights = (int) ChronoUnit.DAYS.between(in, out);
        double factor = createReservation("", "", "", in, out, type, 0, "CONFIRMED").getPriceFactor();
        return room.calculateTotal(nights) * factor;
    }

    // Returns an error message if the booking is not allowed, otherwise null.
    // excludeId is the reservation being edited (so it does not clash with itself).
    public String validate(String roomId, LocalDate in, LocalDate out, String excludeId) {
        Room room = roomService.findById(roomId);
        if (room == null) {
            return "That room does not exist.";
        }
        if ("MAINTENANCE".equals(room.getStatus())) {
            return "That room is under maintenance.";
        }
        if (in.isBefore(LocalDate.now())) {
            return "Check-in cannot be in the past.";
        }
        if (!out.isAfter(in)) {
            return "Check-out must be after check-in.";
        }
        if (hasOverlap(roomId, in, out, excludeId)) {
            return "That room is already booked for these dates.";
        }
        return null;
    }

    // Two stays overlap when each one starts before the other ends
    public boolean hasOverlap(String roomId, LocalDate in, LocalDate out, String excludeId) {
        for (Reservation r : getAllReservations()) {
            if (r.isActive() && r.getRoomId().equals(roomId) && !r.getId().equals(excludeId)
                    && in.isBefore(r.getCheckOut()) && out.isAfter(r.getCheckIn())) {
                return true;
            }
        }
        return false;
    }

    // CREATE
    public void addReservation(Reservation reservation) {
        FileHandler.appendLine(FILE_NAME, reservation.toFileString());
    }

    // READ
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        for (String line : FileHandler.readAllLines(FILE_NAME)) {
            Reservation r = lineToReservation(line);
            if (r != null) {
                list.add(r);
            }
        }
        return list;
    }

    public Reservation findById(String id) {
        for (Reservation r : getAllReservations()) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public List<Reservation> getByUser(String userId) {
        List<Reservation> list = new ArrayList<>();
        for (Reservation r : getAllReservations()) {
            if (r.getUserId().equals(userId)) {
                list.add(r);
            }
        }
        return list;
    }

    // READ (search): admins search everything, guests only their own bookings
    public List<Reservation> searchReservations(String keyword, User viewer) {
        List<Reservation> source = isAdmin(viewer) ? getAllReservations() : getByUser(viewer.getId());
        if (keyword == null || keyword.trim().isEmpty()) {
            return source;
        }
        String key = keyword.trim().toLowerCase();
        List<Reservation> results = new ArrayList<>();
        for (Reservation r : source) {
            if (r.getId().toLowerCase().contains(key)
                    || r.getUserId().toLowerCase().contains(key)
                    || r.getRoomId().toLowerCase().contains(key)
                    || r.getType().toLowerCase().contains(key)
                    || r.getStatus().toLowerCase().contains(key)) {
                results.add(r);
            }
        }
        return results;
    }

    // UPDATE: rewrite the file, swapping the matching reservation's line
    public boolean updateReservation(Reservation updated) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (Reservation r : getAllReservations()) {
            if (r.getId().equals(updated.getId())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(r.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    // UPDATE (cancel): keeps the record, marks it CANCELLED, returns the fee kept
    public double cancelReservation(String id) {
        Reservation r = findById(id);
        if (r == null || !r.isActive()) {
            return 0;
        }
        double fee = r.calculateCancellationFee(); // polymorphic call
        r.setStatus("CANCELLED");
        updateReservation(r);
        return fee;
    }

    // DELETE: remove the record completely (admin only, checked in the servlet)
    public boolean deleteReservation(String id) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (Reservation r : getAllReservations()) {
            if (r.getId().equals(id)) {
                found = true;
            } else {
                lines.add(r.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    // Admins can see everything; guests only their own reservations
    public boolean canAccess(User user, Reservation r) {
        return isAdmin(user) || r.getUserId().equals(user.getId());
    }

    public boolean isAdmin(User user) {
        return user instanceof AdminUser;
    }

    // Gives B001, B002, ...
    public String generateNextId() {
        int max = 0;
        for (Reservation r : getAllReservations()) {
            try {
                int number = Integer.parseInt(r.getId().substring(1));
                if (number > max) {
                    max = number;
                }
            } catch (NumberFormatException e) {
                // ignore badly formatted ids
            }
        }
        return String.format("B%03d", max + 1);
    }

    // Turns a file line into the right Reservation object
    private Reservation lineToReservation(String line) {
        String[] p = line.split(",");
        if (p.length < 8) {
            return null;
        }
        try {
            return createReservation(p[0], p[1], p[2], LocalDate.parse(p[3]),
                    LocalDate.parse(p[4]), p[5], Double.parseDouble(p[6]), p[7]);
        } catch (Exception e) {
            return null; // skip a corrupted line
        }
    }
}