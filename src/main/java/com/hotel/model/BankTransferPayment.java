package com.hotel.model;

import java.time.LocalDate;

public class BankTransferPayment extends Payment {

    public BankTransferPayment(String id, String reservationId, String userId,
                               double amount, LocalDate date, String status) {
        super(id, reservationId, userId, amount, date, status);
    }

    @Override public String getType() { return "BANK_TRANSFER"; }
    @Override public double getFeeRate() { return 0.01; } // 1% transfer fee
}
