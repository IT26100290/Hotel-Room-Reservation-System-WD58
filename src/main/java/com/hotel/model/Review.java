package com.hotel.model;

import java.time.LocalDate;

// Abstract base class: a review is either Public or Verified (abstraction)
public abstract class Review {

    // private fields (encapsulation)
    private String id;       // F001, F002 ...
    private String userId;   // who wrote it, e.g. U001
    private String roomId;   // the room being reviewed, e.g. R101
    private int rating;      // 1 to 5
    private String comment;
    private LocalDate date;

    public Review(String id, String userId, String roomId,
                  int rating, String comment, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    // Every subclass MUST provide these (polymorphism)
    public abstract String getType();
    public abstract double getWeight();   // how much this review counts in the average
    public abstract String getBadge();    // text shown next to the review

    // How much this review adds to a room's weighted rating
    public double getWeightedRating() {
        return rating * getWeight();
    }

    // One line for reviews.txt. Commas in the comment are replaced by a code
    // so they cannot break the comma-separated format.
    // Example: F001,U001,R101,VERIFIED,5,2026-11-14,Great room&comma; very clean
    public String toFileString() {
        String safeComment = comment.replace(",", "&comma;").replace("\n", " ").replace("\r", " ");
        return id + "," + userId + "," + roomId + "," + getType() + ","
                + rating + "," + date + "," + safeComment;
    }
}
