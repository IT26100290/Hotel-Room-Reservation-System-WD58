package com.hotel.service;

import com.hotel.model.DeluxeRoom;
import com.hotel.model.Room;
import com.hotel.model.StandardRoom;
import com.hotel.model.Suite;
import com.hotel.util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class RoomService {

    private static final String FILE_NAME = "rooms.txt";

    // Builds the right subclass for a type (polymorphism: all are Rooms)
    public static Room createRoom(String id, String roomNumber, String type,
                                  double price, String status) {
        switch (type) {
            case "DELUXE": return new DeluxeRoom(id, roomNumber, price, status);
            case "SUITE":  return new Suite(id, roomNumber, price, status);
            default:       return new StandardRoom(id, roomNumber, price, status);
        }
    }

    // CREATE: add one line to the end of rooms.txt
    public void addRoom(Room room) {
        FileHandler.appendLine(FILE_NAME, room.toFileString());
    }

    // READ: load every line and turn each into a Room object
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        for (String line : FileHandler.readAllLines(FILE_NAME)) {
            Room room = lineToRoom(line);
            if (room != null) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    public Room findById(String id) {
        for (Room room : getAllRooms()) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }

    public boolean roomNumberExists(String roomNumber) {
        for (Room room : getAllRooms()) {
            if (room.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                return true;
            }
        }
        return false;
    }

    // READ (search): matches part of id, room number, type or status
    public List<Room> searchRooms(String keyword) {
        List<Room> results = new ArrayList<>();
        String key = keyword.toLowerCase();
        for (Room room : getAllRooms()) {
            if (room.getId().toLowerCase().contains(key)
                    || room.getRoomNumber().toLowerCase().contains(key)
                    || room.getType().toLowerCase().contains(key)
                    || room.getStatus().toLowerCase().contains(key)) {
                results.add(room);
            }
        }
        return results;
    }

    // UPDATE: rewrite the file, swapping the matching room's line
    public boolean updateRoom(Room updated) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (Room room : getAllRooms()) {
            if (room.getId().equals(updated.getId())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(room.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    // DELETE: rewrite the file without the matching room
    public boolean deleteRoom(String id) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (Room room : getAllRooms()) {
            if (room.getId().equals(id)) {
                found = true;
            } else {
                lines.add(room.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    // Gives R101, R102, ... (starts at R101)
    public String generateNextId() {
        int max = 100;
        for (Room room : getAllRooms()) {
            try {
                int number = Integer.parseInt(room.getId().substring(1));
                if (number > max) {
                    max = number;
                }
            } catch (NumberFormatException e) {
                // ignore badly formatted ids
            }
        }
        return "R" + (max + 1);
    }

    // Turns "R101,101,DELUXE,15000.0,AVAILABLE" into the right Room object
    private Room lineToRoom(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            return null;
        }
        try {
            return createRoom(parts[0], parts[1], parts[2],
                    Double.parseDouble(parts[3]), parts[4]);
        } catch (NumberFormatException e) {
            return null; // skip a corrupted line
        }
    }
}