package com.hotel.service;

import com.hotel.model.AdminUser;
import com.hotel.model.BankTransferPayment;
import com.hotel.model.CardPayment;
import com.hotel.model.CashPayment;
import com.hotel.model.Payment;
import com.hotel.model.Reservation;
import com.hotel.model.User;
import com.hotel.util.FileHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {

    private static final String FILE_NAME = "payments.txt";

    private ReservationService reservationService = new ReservationService();

    // Builds the right subclass for a payment method (polymorphism: all are Payments)
    public static Payment createPayment(String id, String reservationId, String userId,
                                        String type, double amount, LocalDate date, String status) {
        switch (type) {
            case "CASH":
                return new CashPayment(id, reservationId, userId, amount, date, status);
            case "BANK_TRANSFER":
                return new BankTransferPayment(id, reservationId, userId, amount, date, status);
            default:
                return new CardPayment(id, reservationId, userId, amount, date, status);
        }
    }

    // Returns an error message if this payment is not allowed, otherwise null
    public String validate(String reservationId, User user) {
        Reservation r = reservationService.findById(reservationId);
        if (r == null) {
            return "That reservation does not exist.";
        }
        if (!reservationService.canAccess(user, r)) {
            return "You cannot pay for someone else's reservation.";
        }
        if (!r.isActive()) {
            return "A cancelled reservation cannot be paid.";
        }
        if (findPaidByReservation(reservationId) != null) {
            return "That reservation is already paid.";
        }
        return null;
    }

    // Reservations the user can still pay: active and not yet paid
    public List<Reservation> getPayableReservations(User user) {
        List<Reservation> list = new ArrayList<>();
        for (Reservation r : reservationService.searchReservations(null, user)) {
            if (r.isActive() && findPaidByReservation(r.getId()) == null) {
                list.add(r);
            }
        }
        return list;
    }

    // CREATE
    public void addPayment(Payment payment) {
        FileHandler.appendLine(FILE_NAME, payment.toFileString());
    }

    // READ
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        for (String line : FileHandler.readAllLines(FILE_NAME)) {
            Payment p = lineToPayment(line);
            if (p != null) {
                list.add(p);
            }
        }
        return list;
    }

    public Payment findById(String id) {
        for (Payment p : getAllPayments()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    // The PAID payment of a reservation, or null if it is unpaid
    public Payment findPaidByReservation(String reservationId) {
        for (Payment p : getAllPayments()) {
            if (p.getReservationId().equals(reservationId) && p.isPaid()) {
                return p;
            }
        }
        return null;
    }

    public List<Payment> getByUser(String userId) {
        List<Payment> list = new ArrayList<>();
        for (Payment p : getAllPayments()) {
            if (p.getUserId().equals(userId)) {
                list.add(p);
            }
        }
        return list;
    }

    // READ (search): admins search everything, guests only their own payments
    public List<Payment> searchPayments(String keyword, User viewer) {
        List<Payment> source = isAdmin(viewer) ? getAllPayments() : getByUser(viewer.getId());
        if (keyword == null || keyword.trim().isEmpty()) {
            return source;
        }
        String key = keyword.trim().toLowerCase();
        List<Payment> results = new ArrayList<>();
        for (Payment p : source) {
            if (p.getId().toLowerCase().contains(key)
                    || p.getReservationId().toLowerCase().contains(key)
                    || p.getUserId().toLowerCase().contains(key)
                    || p.getType().toLowerCase().contains(key)
                    || p.getStatus().toLowerCase().contains(key)) {
                results.add(p);
            }
        }
        return results;
    }

    // UPDATE: rewrite the file, swapping the matching payment's line
    public boolean updatePayment(Payment updated) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (Payment p : getAllPayments()) {
            if (p.getId().equals(updated.getId())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(p.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    // UPDATE (refund): keep the record, mark it REFUNDED
    public boolean refundPayment(String id) {
        Payment p = findById(id);
        if (p == null || !p.isPaid()) {
            return false;
        }
        p.setStatus("REFUNDED");
        return updatePayment(p);
    }

    // DELETE: remove the record completely (admin only, checked in the servlet)
    public boolean deletePayment(String id) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (Payment p : getAllPayments()) {
            if (p.getId().equals(id)) {
                found = true;
            } else {
                lines.add(p.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    // Admins can see everything; guests only their own payments
    public boolean canAccess(User user, Payment p) {
        return isAdmin(user) || p.getUserId().equals(user.getId());
    }

    public boolean isAdmin(User user) {
        return user instanceof AdminUser;
    }

    // Gives P001, P002, ...
    public String generateNextId() {
        int max = 0;
        for (Payment p : getAllPayments()) {
            try {
                int number = Integer.parseInt(p.getId().substring(1));
                if (number > max) {
                    max = number;
                }
            } catch (NumberFormatException e) {
                // ignore badly formatted ids
            }
        }
        return String.format("P%03d", max + 1);
    }

    // Turns a file line into the right Payment object
    private Payment lineToPayment(String line) {
        String[] p = line.split(",");
        if (p.length < 7) {
            return null;
        }
        try {
            return createPayment(p[0], p[1], p[2], p[3], Double.parseDouble(p[4]),
                    LocalDate.parse(p[5]), p[6]);
        } catch (Exception e) {
            return null; // skip a corrupted line
        }
    }
}
