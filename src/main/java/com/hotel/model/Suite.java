package com.hotel.model;

public class Suite extends Room {

    public Suite(String id, String roomNumber, double pricePerNight, String status) {
        super(id, roomNumber, pricePerNight, status);
    }

    @Override
    public String getType() { return "SUITE"; }

    // Suites: price x nights plus a fixed 5000 butler fee
    @Override
    public double calculateTotal(int nights) {
        return getPricePerNight() * nights + 5000;
    }
}