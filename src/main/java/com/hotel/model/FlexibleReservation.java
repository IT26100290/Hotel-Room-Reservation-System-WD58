package com.hotel.model;

import java.time.LocalDate;

public class FlexibleReservation extends Reservation {

    public FlexibleReservation(String id, String userId, String roomId,
                               LocalDate checkIn, LocalDate checkOut,
                               double totalPrice, String status) {
        super(id, userId, roomId, checkIn, checkOut, totalPrice, status);
    }

    @Override public String getType() { return "FLEXIBLE"; }
    @Override public double getPriceFactor() { return 1.05; }          // 5% extra for flexibility
    @Override public double getCancellationFeeRate() { return 0.0; }   // free cancellation
}