package com.hotel.model;


public class RegularGuest extends User {

    public RegularGuest(String id, String username, String password, String email) {
        super(id, username, password, email); // calls the User constructor
    }

    @Override
    public String getMembershipType() {
        return "REGULAR";
    }

    @Override
    public double getDiscountRate() {
        return 0.0; // regular guests get no discount
    }
}