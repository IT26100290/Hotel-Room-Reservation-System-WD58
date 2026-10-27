package com.hotel.servlet;

import com.hotel.model.User;
import com.hotel.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/deleteReservation")
public class DeleteReservationServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationService();

    // DELETE: remove the record completely. Admin only.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loggedUser");
        if (user == null || !reservationService.isAdmin(user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String id = request.getParameter("id");
        if (id != null) {
            reservationService.deleteReservation(id);
        }
        response.sendRedirect(request.getContextPath() + "/reservations");
    }
}