package com.hotel.model;

import java.time.LocalDate;

public class VerifiedReview extends Review {

    public VerifiedReview(String id, String userId, String roomId,
                          int rating, String comment, LocalDate date) {
        super(id, userId, roomId, rating, comment, date);
    }

    @Override public String getType() { return "VERIFIED"; }
    @Override public double getWeight() { return 2.0; }          // counts double
    @Override public String getBadge() { return "Verified stay"; }
}
