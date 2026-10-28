package com.hotel.servlet;

import com.hotel.model.Review;
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
import java.time.LocalDate;

@WebServlet("/addReview")
public class AddReviewServlet extends HttpServlet {

    private ReviewService reviewService = new ReviewService();
    private RoomService roomService = new RoomService();

    // GET /addReview  or  /addReview?roomId=R101 : show the form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (getLoggedUser(request) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        showForm(request, response, request.getParameter("roomId"));
    }

    // POST: validate and save the review (CREATE)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String roomId = request.getParameter("roomId");
        String comment = request.getParameter("comment");
        int rating;
        try {
            rating = Integer.parseInt(request.getParameter("rating"));
        } catch (NumberFormatException e) {
            rating = 0;
        }

        String error = reviewService.validate(user.getId(), roomId, rating, comment, null);
        if (error != null) {
            request.setAttribute("error", error);
            showForm(request, response, roomId);
            return;
        }

        // the type (PUBLIC or VERIFIED) is decided by the system, not the user
        String type = reviewService.decideType(user.getId(), roomId);
        Review review = ReviewService.createReview(reviewService.generateNextId(),
                user.getId(), roomId, type, rating, comment.trim(), LocalDate.now());
        reviewService.addReview(review);
        response.sendRedirect(request.getContextPath() + "/reviews?roomId=" + roomId);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String roomId)
            throws ServletException, IOException {
        request.setAttribute("rooms", roomService.getAllRooms());
        request.setAttribute("selectedRoomId", roomId == null ? "" : roomId);
        request.getRequestDispatcher("/feedback/addReview.jsp").forward(request, response);
    }

    private User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (User) session.getAttribute("loggedUser");
    }
}
