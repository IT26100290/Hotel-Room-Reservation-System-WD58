package com.hotel.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Abstract base class: a reservation is always one of the three booking types
// (abstraction).
public abstract class Reservation {

    // private fields (encapsulation)
    private String id;          // B001, B002 ...
    private String userId;      // the guest, e.g. U001
    private String roomId;      // the room, e.g. R101
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalPrice;
    private String status;      // CONFIRMED or CANCELLED

    public Reservation(String id, String userId, String roomId,
                       LocalDate checkIn, LocalDate checkOut,
                       double totalPrice, String status) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public boolean isActive() {
        return "CONFIRMED".equals(status);
    }

    // Every subclass MUST provide these (polymorphism)
    public abstract String getType();
    public abstract double getPriceFactor();          // multiplies the room price
    public abstract double getCancellationFeeRate();  // share of the total kept on cancel

    // Same call, different result for each subclass (polymorphism)
    public double calculateCancellationFee() {
        return totalPrice * getCancellationFeeRate();
    }

    // One line for reservations.txt
    // Example: B001,U001,R101,2026-11-01,2026-11-04,STANDARD,45000.0,CONFIRMED
    public String toFileString() {
        return id + "," + userId + "," + roomId + "," + checkIn + "," + checkOut
                + "," + getType() + "," + totalPrice + "," + status;
    }
}