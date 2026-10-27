package com.hotel.servlet;

import com.hotel.model.Reservation;
import com.hotel.model.User;
import com.hotel.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/cancelReservation")
public class CancelReservationServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationService();

    // Marks the reservation CANCELLED (the owner or an admin may do this)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loggedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Reservation r = reservationService.findById(request.getParameter("id"));
        if (r != null && reservationService.canAccess(user, r)) {
            reservationService.cancelReservation(r.getId());
        }
        response.sendRedirect(request.getContextPath() + "/reservations");
    }
}