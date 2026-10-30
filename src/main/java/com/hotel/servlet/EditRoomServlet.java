package com.hotel.servlet;

import com.hotel.model.Room;
import com.hotel.model.AdminUser;
import com.hotel.service.RoomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/editRoom")
public class EditRoomServlet extends HttpServlet {

    private RoomService roomService = new RoomService();

    // GET /editRoom?id=R101 : show the form filled with the room's details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object loggedUser = request.getSession().getAttribute("loggedUser");
        if (!(loggedUser instanceof AdminUser)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Room room = roomService.findById(request.getParameter("id"));
        if (room == null) {
            response.sendRedirect(request.getContextPath() + "/rooms");
            return;
        }
        request.setAttribute("room", room);
        request.getRequestDispatcher("/room/editRoom.jsp").forward(request, response);
    }

    // POST: save the changes (UPDATE)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object loggedUser = request.getSession().getAttribute("loggedUser");
        if (!(loggedUser instanceof AdminUser)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String id = request.getParameter("id");
        String roomNumber = request.getParameter("roomNumber").trim();
        String type = request.getParameter("type");
        String status = request.getParameter("status");

        Room existing = roomService.findById(id);
        if (existing == null) {
            response.sendRedirect(request.getContextPath() + "/rooms");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(request.getParameter("pricePerNight").trim());
        } catch (NumberFormatException e) {
            price = -1;
        }

        String error = null;
        if (roomNumber.isEmpty() || roomNumber.contains(",")) {
            error = "Room number is required and must not contain commas.";
        } else if (price <= 0) {
            error = "Price must be a positive number.";
        } else if (!roomNumber.equalsIgnoreCase(existing.getRoomNumber())
                && roomService.roomNumberExists(roomNumber)) {
            error = "That room number already exists.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("room", existing);
            request.getRequestDispatcher("/room/editRoom.jsp").forward(request, response);
            return;
        }

        roomService.updateRoom(RoomService.createRoom(id, roomNumber, type, price, status));
        response.sendRedirect(request.getContextPath() + "/rooms");
    }
}
