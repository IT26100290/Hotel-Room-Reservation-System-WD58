package com.hotel.model;

import java.time.LocalDate;

public class NonRefundableReservation extends Reservation {

    public NonRefundableReservation(String id, String userId, String roomId,
                                    LocalDate checkIn, LocalDate checkOut,
                                    double totalPrice, String status) {
        super(id, userId, roomId, checkIn, checkOut, totalPrice, status);
    }

    @Override public String getType() { return "NON_REFUNDABLE"; }
    @Override public double getPriceFactor() { return 0.90; }          // 10% cheaper
    @Override public double getCancellationFeeRate() { return 1.0; }   // whole amount kept
}