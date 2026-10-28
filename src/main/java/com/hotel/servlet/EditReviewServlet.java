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

@WebServlet("/editReview")
public class EditReviewServlet extends HttpServlet {

    private ReviewService reviewService = new ReviewService();

    // GET /editReview?id=F001 : show the form with the current rating and comment
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        Review r = reviewService.findById(request.getParameter("id"));
        if (user == null || r == null || !reviewService.canModify(user, r)) {
            response.sendRedirect(request.getContextPath() + "/reviews");
            return;
        }
        request.setAttribute("review", r);
        request.getRequestDispatcher("/feedback/editReview.jsp").forward(request, response);
    }

    // POST: save the new rating and comment (UPDATE). Room, author and type stay the same.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        Review existing = reviewService.findById(request.getParameter("id"));
        if (user == null || existing == null || !reviewService.canModify(user, existing)) {
            response.sendRedirect(request.getContextPath() + "/reviews");
            return;
        }

        String comment = request.getParameter("comment");
        int rating;
        try {
            rating = Integer.parseInt(request.getParameter("rating"));
        } catch (NumberFormatException e) {
            rating = 0;
        }

        String error = reviewService.validate(existing.getUserId(), existing.getRoomId(),
                rating, comment, existing.getId());
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("review", existing);
            request.getRequestDispatcher("/feedback/editReview.jsp").forward(request, response);
            return;
        }

        reviewService.updateReview(ReviewService.createReview(existing.getId(),
                existing.getUserId(), existing.getRoomId(), existing.getType(),
                rating, comment.trim(), existing.getDate()));
        response.sendRedirect(request.getContextPath() + "/reviews?roomId=" + existing.getRoomId());
    }

    private User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (User) session.getAttribute("loggedUser");
    }
}
