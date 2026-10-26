package com.hotel.model;

// Abstract base class: you cannot create a plain Room, only a StandardRoom,
// DeluxeRoom or Suite (abstraction).
public abstract class Room {

    // private fields: only reachable through getters/setters (encapsulation)
    private String id;
    private String roomNumber;
    private double pricePerNight;
    private String status; // AVAILABLE, BOOKED or MAINTENANCE

    public Room(String id, String roomNumber, double pricePerNight, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Every subclass MUST provide these (polymorphism)
    public abstract String getType();
    public abstract double calculateTotal(int nights);

    // Turns the object into one line for rooms.txt
    // Example: R101,101,DELUXE,15000.0,AVAILABLE
    public String toFileString() {
        return id + "," + roomNumber + "," + getType() + "," + pricePerNight + "," + status;
    }
}