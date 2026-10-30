package com.hotel.servlet;

import com.hotel.model.AdminUser;
import com.hotel.service.RoomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteRoom")
public class DeleteRoomServlet extends HttpServlet {

    private RoomService roomService = new RoomService();

    // DELETE: remove the room whose id was sent by the form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object loggedUser = request.getSession().getAttribute("loggedUser");
        if (!(loggedUser instanceof AdminUser)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String id = request.getParameter("id");
        if (id != null) {
            roomService.deleteRoom(id);
        }
        response.sendRedirect(request.getContextPath() + "/rooms");
    }
}
