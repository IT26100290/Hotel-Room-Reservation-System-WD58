package com.hotel.model;

import java.time.LocalDate;

// Inheritance: PublicReview "is a" Review
public class PublicReview extends Review {

    public PublicReview(String id, String userId, String roomId,
                        int rating, String comment, LocalDate date) {
        super(id, userId, roomId, rating, comment, date);
    }

    @Override public String getType() { return "PUBLIC"; }
    @Override public double getWeight() { return 1.0; }          // counts normally
    @Override public String getBadge() { return "Guest review"; }
}
