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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/reservations")
public class ReservationListServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationService();
    private RoomService roomService = new RoomService();

    // READ: logged-in guests see their bookings, admins see all
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loggedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String keyword = request.getParameter("keyword");
        List<Reservation> reservations = reservationService.searchReservations(keyword, user);

        // room id -> room number, so the page can show "101" instead of "R101"
        Map<String, String> roomNumbers = new HashMap<>();
        for (Room room : roomService.getAllRooms()) {
            roomNumbers.put(room.getId(), room.getRoomNumber());
        }

        request.setAttribute("reservations", reservations);
        request.setAttribute("roomNumbers", roomNumbers);
        request.setAttribute("isAdmin", reservationService.isAdmin(user));
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.getRequestDispatcher("/reservation/reservationList.jsp").forward(request, response);
    }
}