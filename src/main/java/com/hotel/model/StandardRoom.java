package com.hotel.model;

// Inheritance: StandardRoom "is a" Room
public class StandardRoom extends Room {

    public StandardRoom(String id, String roomNumber, double pricePerNight, String status) {
        super(id, roomNumber, pricePerNight, status); // calls the Room constructor
    }

    @Override
    public String getType() { return "STANDARD"; }

    // Standard rooms: plain price x nights
    @Override
    public double calculateTotal(int nights) {
        return getPricePerNight() * nights;
    }
}