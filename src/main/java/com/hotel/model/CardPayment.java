package com.hotel.model;

import java.time.LocalDate;

// Inheritance: CardPayment "is a" Payment
public class CardPayment extends Payment {

    public CardPayment(String id, String reservationId, String userId,
                       double amount, LocalDate date, String status) {
        super(id, reservationId, userId, amount, date, status);
    }

    @Override public String getType() { return "CARD"; }
    @Override public double getFeeRate() { return 0.025; } // 2.5% card fee
}
