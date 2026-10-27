package com.hotel.servlet;

import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.service.ReservationService;
import com.hotel.service.RoomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/editReservation")
public class EditReservationServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationService();
    private RoomService roomService = new RoomService();

    // GET /editReservation?id=B001 : show the form with the current dates
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        Reservation r = (user == null) ? null : reservationService.findById(request.getParameter("id"));
        if (user == null || r == null || !reservationService.canAccess(user, r) || !r.isActive()) {
            response.sendRedirect(request.getContextPath() + "/reservations");
            return;
        }
        request.setAttribute("reservation", r);
        request.getRequestDispatcher("/reservation/editReservation.jsp").forward(request, response);
    }

    // POST: change dates and type, recalculate the price (UPDATE)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        Reservation existing = (user == null) ? null : reservationService.findById(request.getParameter("id"));
        if (user == null || existing == null || !reservationService.canAccess(user, existing)
                || !existing.isActive()) {
            response.sendRedirect(request.getContextPath() + "/reservations");
            return;
        }

        String type = request.getParameter("type");
        LocalDate in;
        LocalDate out;
        try {
            in = LocalDate.parse(request.getParameter("checkIn"));
            out = LocalDate.parse(request.getParameter("checkOut"));
        } catch (DateTimeParseException | NullPointerException e) {
            fail(request, response, existing, "Please choose valid dates.");
            return;
        }

        String error = reservationService.validate(existing.getRoomId(), in, out, existing.getId());
        if (error != null) {
            fail(request, response, existing, error);
            return;
        }

        Room room = roomService.findById(existing.getRoomId());
        double total = reservationService.calculatePrice(room, type, in, out);
        reservationService.updateReservation(ReservationService.createReservation(
                existing.getId(), existing.getUserId(), existing.getRoomId(),
                in, out, type, total, "CONFIRMED"));
        response.sendRedirect(request.getContextPath() + "/reservations");
    }

    private void fail(HttpServletRequest request, HttpServletResponse response,
                      Reservation existing, String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        request.setAttribute("reservation", existing);
        request.getRequestDispatcher("/reservation/editReservation.jsp").forward(request, response);
    }

    private User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (User) session.getAttribute("loggedUser");
    }
}