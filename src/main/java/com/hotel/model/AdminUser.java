package com.hotel.model;


public class AdminUser extends User {

    public AdminUser(String id, String username, String password, String email) {
        super(id, username, password, email);
    }

    @Override
    public String getMembershipType() {
        return "ADMIN";
    }

    @Override
    public double getDiscountRate() {
        return 0.0;
    }

    public boolean canManageUsers() {
        return true;
    }
}