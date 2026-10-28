package com.hotel.servlet;

import com.hotel.model.Review;
import com.hotel.model.User;
import com.hotel.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/deleteReview")
public class DeleteReviewServlet extends HttpServlet {

    private ReviewService reviewService = new ReviewService();

    // DELETE: the author or an admin may remove a review
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loggedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Review r = reviewService.findById(request.getParameter("id"));
        if (r != null && reviewService.canModify(user, r)) {
            reviewService.deleteReview(r.getId());
        }
        response.sendRedirect(request.getContextPath() + "/reviews");
    }
}
