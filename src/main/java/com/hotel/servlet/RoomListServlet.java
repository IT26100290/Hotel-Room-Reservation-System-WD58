package com.hotel.servlet;

import com.hotel.model.Room;
import com.hotel.service.RoomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/rooms")
public class RoomListServlet extends HttpServlet {

    private RoomService roomService = new RoomService();

    // READ: show all rooms, or only those matching the search keyword
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Room> rooms;
        if (keyword != null && !keyword.trim().isEmpty()) {
            rooms = roomService.searchRooms(keyword.trim());
        } else {
            rooms = roomService.getAllRooms();
        }
        request.setAttribute("rooms", rooms);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.getRequestDispatcher("/room/roomList.jsp").forward(request, response);
    }
}