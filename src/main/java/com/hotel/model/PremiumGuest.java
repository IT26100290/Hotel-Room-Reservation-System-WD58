package com.hotel.model;

public class PremiumGuest extends User {

    public PremiumGuest(String id, String username, String password, String email) {
        super(id, username, password, email);
    }

    @Override
    public String getMembershipType() {
        return "PREMIUM";
    }

    @Override
    public double getDiscountRate() {
        return 0.10;
    }
}