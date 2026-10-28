package com.hotel.model;

import java.time.LocalDate;

public class CashPayment extends Payment {

    public CashPayment(String id, String reservationId, String userId,
                       double amount, LocalDate date, String status) {
        super(id, reservationId, userId, amount, date, status);
    }

    @Override public String getType() { return "CASH"; }
    @Override public double getFeeRate() { return 0.0; } // no fee for cash
}
