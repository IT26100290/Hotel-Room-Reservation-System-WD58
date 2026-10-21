package com.hotel.service;

import com.hotel.model.AdminUser;
import com.hotel.model.PremiumGuest;
import com.hotel.model.RegularGuest;
import com.hotel.model.User;
import com.hotel.util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final String FILE_NAME = "users.txt";


    public void addUser(User user) {
        FileHandler.appendLine(FILE_NAME, user.toFileString());
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (String line : FileHandler.readAllLines(FILE_NAME)) {
            User user = lineToUser(line);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    public User findById(String id) {
        for (User user : getAllUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User findByUsername(String username) {
        for (User user : getAllUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> searchUsers(String keyword) {
        List<User> results = new ArrayList<>();
        String key = keyword.toLowerCase();
        for (User user : getAllUsers()) {
            if (user.getId().toLowerCase().contains(key)
                    || user.getUsername().toLowerCase().contains(key)
                    || user.getEmail().toLowerCase().contains(key)) {
                results.add(user);
            }
        }
        return results;
    }

    public boolean updateUser(User updated) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (User user : getAllUsers()) {
            if (user.getId().equals(updated.getId())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(user.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    public boolean deleteUser(String id) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (User user : getAllUsers()) {
            if (user.getId().equals(id)) {
                found = true;
            } else {
                lines.add(user.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public String generateNextId() {
        int max = 0;
        for (User user : getAllUsers()) {
            try {
                int number = Integer.parseInt(user.getId().substring(1));
                if (number > max) {
                    max = number;
                }
            } catch (NumberFormatException e) {
                // ignore badly formatted ids
            }
        }
        return String.format("U%03d", max + 1);
    }

    private User lineToUser(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            return null;
        }
        switch (parts[4]) {
            case "PREMIUM":
                return new PremiumGuest(parts[0], parts[1], parts[2], parts[3]);
            case "ADMIN":
                return new AdminUser(parts[0], parts[1], parts[2], parts[3]);
            default:
                return new RegularGuest(parts[0], parts[1], parts[2], parts[3]);
        }
    }
}