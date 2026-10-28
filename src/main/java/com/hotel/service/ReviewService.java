package com.hotel.service;

import com.hotel.model.AdminUser;
import com.hotel.model.PublicReview;
import com.hotel.model.Reservation;
import com.hotel.model.Review;
import com.hotel.model.User;
import com.hotel.model.VerifiedReview;
import com.hotel.util.FileHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReviewService {

    private static final String FILE_NAME = "reviews.txt";

    private RoomService roomService = new RoomService();
    private ReservationService reservationService = new ReservationService();

    // Builds the right subclass for a review type (polymorphism: all are Reviews)
    public static Review createReview(String id, String userId, String roomId, String type,
                                      int rating, String comment, LocalDate date) {
        if ("VERIFIED".equals(type)) {
            return new VerifiedReview(id, userId, roomId, rating, comment, date);
        }
        return new PublicReview(id, userId, roomId, rating, comment, date);
    }

    // A review is VERIFIED when the guest has a confirmed reservation for that room
    public String decideType(String userId, String roomId) {
        for (Reservation r : reservationService.getByUser(userId)) {
            if (r.getRoomId().equals(roomId) && r.isActive()) {
                return "VERIFIED";
            }
        }
        return "PUBLIC";
    }

    // Returns an error message if the review is not allowed, otherwise null.
    // excludeId is the review being edited (so it does not clash with itself).
    public String validate(String userId, String roomId, int rating, String comment, String excludeId) {
        if (roomService.findById(roomId) == null) {
            return "That room does not exist.";
        }
        if (rating < 1 || rating > 5) {
            return "Rating must be between 1 and 5.";
        }
        if (comment == null || comment.trim().isEmpty()) {
            return "Please write a comment.";
        }
        if (comment.length() > 300) {
            return "Comment must be 300 characters or fewer.";
        }
        for (Review r : getAllReviews()) {
            if (r.getUserId().equals(userId) && r.getRoomId().equals(roomId)
                    && !r.getId().equals(excludeId)) {
                return "You have already reviewed this room. Edit your review instead.";
            }
        }
        return null;
    }

    // CREATE
    public void addReview(Review review) {
        FileHandler.appendLine(FILE_NAME, review.toFileString());
    }

    // READ
    public List<Review> getAllReviews() {
        List<Review> list = new ArrayList<>();
        for (String line : FileHandler.readAllLines(FILE_NAME)) {
            Review r = lineToReview(line);
            if (r != null) {
                list.add(r);
            }
        }
        return list;
    }

    public Review findById(String id) {
        for (Review r : getAllReviews()) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    // READ (search): optional room filter plus keyword over id, room, type, comment
    public List<Review> searchReviews(String roomId, String keyword) {
        List<Review> results = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.trim().toLowerCase();
        for (Review r : getAllReviews()) {
            boolean roomOk = roomId == null || roomId.isEmpty() || r.getRoomId().equals(roomId);
            boolean keyOk = key.isEmpty()
                    || r.getId().toLowerCase().contains(key)
                    || r.getRoomId().toLowerCase().contains(key)
                    || r.getType().toLowerCase().contains(key)
                    || r.getComment().toLowerCase().contains(key);
            if (roomOk && keyOk) {
                results.add(r);
            }
        }
        return results;
    }

    // Weighted average: verified reviews count double (polymorphic getWeight)
    public double getAverageRating(List<Review> reviews) {
        double sum = 0;
        double weights = 0;
        for (Review r : reviews) {
            sum += r.getWeightedRating();
            weights += r.getWeight();
        }
        return weights == 0 ? 0 : sum / weights;
    }

    // UPDATE: rewrite the file, swapping the matching review's line
    public boolean updateReview(Review updated) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (Review r : getAllReviews()) {
            if (r.getId().equals(updated.getId())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(r.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    // DELETE: rewrite the file without the matching review
    public boolean deleteReview(String id) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        for (Review r : getAllReviews()) {
            if (r.getId().equals(id)) {
                found = true;
            } else {
                lines.add(r.toFileString());
            }
        }
        if (found) {
            FileHandler.writeAllLines(FILE_NAME, lines);
        }
        return found;
    }

    // The author or an admin may change or delete a review
    public boolean canModify(User user, Review r) {
        return user != null && (isAdmin(user) || r.getUserId().equals(user.getId()));
    }

    public boolean isAdmin(User user) {
        return user instanceof AdminUser;
    }

    // Gives F001, F002, ...
    public String generateNextId() {
        int max = 0;
        for (Review r : getAllReviews()) {
            try {
                int number = Integer.parseInt(r.getId().substring(1));
                if (number > max) {
                    max = number;
                }
            } catch (NumberFormatException e) {
                // ignore badly formatted ids
            }
        }
        return String.format("F%03d", max + 1);
    }

    // Stops a comment from injecting HTML into the page
    public static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    // Turns a file line into the right Review object
    private Review lineToReview(String line) {
        String[] p = line.split(",", 7);
        if (p.length < 7) {
            return null;
        }
        try {
            return createReview(p[0], p[1], p[2], p[3], Integer.parseInt(p[4]),
                    p[6].replace("&comma;", ","), LocalDate.parse(p[5]));
        } catch (Exception e) {
            return null; // skip a corrupted line
        }
    }
}
