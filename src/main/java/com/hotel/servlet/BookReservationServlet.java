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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/book")
public class BookReservationServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationService();
    private RoomService roomService = new RoomService();

    // GET /book  or  /book?roomId=R101 : show the booking form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (getLoggedUser(request) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        showForm(request, response, request.getParameter("roomId"));
    }

    // POST: validate, work out the price, and save (CREATE)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String roomId = request.getParameter("roomId");
        String type = request.getParameter("type");

        LocalDate in;
        LocalDate out;
        try {
            in = LocalDate.parse(request.getParameter("checkIn"));
            out = LocalDate.parse(request.getParameter("checkOut"));
        } catch (DateTimeParseException | NullPointerException e) {
            request.setAttribute("error", "Please choose valid dates.");
            showForm(request, response, roomId);
            return;
        }

        String error = reservationService.validate(roomId, in, out, null);
        if (error != null) {
            request.setAttribute("error", error);
            showForm(request, response, roomId);
            return;
        }

        Room room = roomService.findById(roomId);
        double total = reservationService.calculatePrice(room, type, in, out);
        Reservation reservation = ReservationService.createReservation(
                reservationService.generateNextId(), user.getId(), roomId, in, out,
                type, total, "CONFIRMED");
        reservationService.addReservation(reservation);
        response.sendRedirect(request.getContextPath() + "/reservations");
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String roomId)
            throws ServletException, IOException {
        // only rooms that are not under maintenance can be booked
        List<Room> bookable = new ArrayList<>();
        for (Room room : roomService.getAllRooms()) {
            if (!"MAINTENANCE".equals(room.getStatus())) {
                bookable.add(room);
            }
        }
        request.setAttribute("rooms", bookable);
        request.setAttribute("selectedRoomId", roomId == null ? "" : roomId);
        request.getRequestDispatcher("/reservation/bookRoom.jsp").forward(request, response);
    }

    private User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (User) session.getAttribute("loggedUser");
    }
}