package com.hotel.servlet;

import com.hotel.model.Review;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.service.ReviewService;
import com.hotel.service.RoomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/reviews")
public class ReviewListServlet extends HttpServlet {

    private ReviewService reviewService = new ReviewService();
    private RoomService roomService = new RoomService();

    // READ: anyone can read reviews; logging in adds the Edit/Delete buttons
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loggedUser");

        String roomId = request.getParameter("roomId");
        String keyword = request.getParameter("keyword");
        List<Review> reviews = reviewService.searchReviews(roomId, keyword);

        // room id -> room number, so the page can show "101" instead of "R101"
        Map<String, String> roomNumbers = new HashMap<>();
        for (Room room : roomService.getAllRooms()) {
            roomNumbers.put(room.getId(), room.getRoomNumber());
        }

        request.setAttribute("reviews", reviews);
        request.setAttribute("average", reviewService.getAverageRating(reviews));
        request.setAttribute("rooms", roomService.getAllRooms());
        request.setAttribute("roomNumbers", roomNumbers);
        request.setAttribute("selectedRoomId", roomId == null ? "" : roomId);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.setAttribute("currentUserId", user == null ? "" : user.getId());
        request.setAttribute("isAdmin", reviewService.isAdmin(user));
        request.getRequestDispatcher("/feedback/reviewList.jsp").forward(request, response);
    }
}
