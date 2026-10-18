package com.hotel.servlet;

import com.hotel.model.AdminUser;
import com.hotel.model.User;
import com.hotel.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserListServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession(false);
        User loggedUser = (session == null) ? null : (User) session.getAttribute("loggedUser");
        if (!(loggedUser instanceof AdminUser)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String keyword = request.getParameter("keyword");
        List<User> users;
        if (keyword != null && !keyword.trim().isEmpty()) {
            users = userService.searchUsers(keyword.trim());
        } else {
            users = userService.getAllUsers();
        }

        request.setAttribute("users", users);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.getRequestDispatcher("/user/userList.jsp").forward(request, response);
    }
}