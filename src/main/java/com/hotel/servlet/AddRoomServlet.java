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

@WebServlet("/addRoom")
public class AddRoomServlet extends HttpServlet {

    private RoomService roomService = new RoomService();

    // GET: show the empty form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object loggedUser = request.getSession().getAttribute("loggedUser");
        if (!(loggedUser instanceof AdminUser)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.getRequestDispatcher("/room/addRoom.jsp").forward(request, response);
    }

    // POST: the form was submitted, so create the room (CREATE)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object loggedUser = request.getSession().getAttribute("loggedUser");
        if (!(loggedUser instanceof AdminUser)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String roomNumber = request.getParameter("roomNumber").trim();
        String type = request.getParameter("type");
        String priceText = request.getParameter("pricePerNight").trim();
        String status = request.getParameter("status");

        if (roomNumber.isEmpty() || roomNumber.contains(",")) {
            showError(request, response, "Room number is required and must not contain commas.");
            return;
        }
        if (roomService.roomNumberExists(roomNumber)) {
            showError(request, response, "That room number already exists.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showError(request, response, "Price must be a number.");
            return;
        }
        if (price <= 0) {
            showError(request, response, "Price must be greater than zero.");
            return;
        }

        Room room = RoomService.createRoom(roomService.generateNextId(),
                roomNumber, type, price, status);
        roomService.addRoom(room);
        response.sendRedirect(request.getContextPath() + "/rooms");
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        request.setAttribute("error", msg);
        request.getRequestDispatcher("/room/addRoom.jsp").forward(request, response);
    }
}
