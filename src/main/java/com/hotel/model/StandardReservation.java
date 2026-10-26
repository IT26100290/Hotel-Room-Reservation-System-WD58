package com.hotel.model;

import java.time.LocalDate;

// Inheritance: StandardReservation "is a" Reservation
public class StandardReservation extends Reservation {

    public StandardReservation(String id, String userId, String roomId,
                               LocalDate checkIn, LocalDate checkOut,
                               double totalPrice, String status) {
        super(id, userId, roomId, checkIn, checkOut, totalPrice, status);
    }

    @Override public String getType() { return "STANDARD"; }
    @Override public double getPriceFactor() { return 1.0; }           // normal price
    @Override public double getCancellationFeeRate() { return 0.20; }  // 20% fee
}