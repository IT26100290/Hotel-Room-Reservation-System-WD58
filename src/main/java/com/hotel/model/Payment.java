package com.hotel.model;

import java.time.LocalDate;

// Abstract base class: a payment is always Card, Cash or Bank Transfer (abstraction)
public abstract class Payment {

    // private fields (encapsulation)
    private String id;            // P001, P002 ...
    private String reservationId; // the booking being paid, e.g. B001
    private String userId;        // who paid, e.g. U001
    private double amount;        // the reservation total
    private LocalDate date;
    private String status;        // PAID or REFUNDED

    public Payment(String id, String reservationId, String userId,
                   double amount, LocalDate date, String status) {
        this.id = id;
        this.reservationId = reservationId;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isPaid() {
        return "PAID".equals(status);
    }

    // Every subclass MUST provide these (polymorphism)
    public abstract String getType();
    public abstract double getFeeRate();

    // Same call, different result per subclass (polymorphism)
    public double calculateFee() {
        return amount * getFeeRate();
    }

    public double getTotalCharged() {
        return amount + calculateFee();
    }

    // One line for payments.txt
    // Example: P001,B001,U001,CARD,24000.0,2026-11-01,PAID
    public String toFileString() {
        return id + "," + reservationId + "," + userId + "," + getType()
                + "," + amount + "," + date + "," + status;
    }
}
