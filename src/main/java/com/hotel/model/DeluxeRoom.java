package com.hotel.model;

public class DeluxeRoom extends Room {

    public DeluxeRoom(String id, String roomNumber, double pricePerNight, String status) {
        super(id, roomNumber, pricePerNight, status);
    }

    @Override
    public String getType() { return "DELUXE"; }

    // Deluxe rooms: price x nights plus a 10% service charge
    @Override
    public double calculateTotal(int nights) {
        return getPricePerNight() * nights * 1.10;
    }
}