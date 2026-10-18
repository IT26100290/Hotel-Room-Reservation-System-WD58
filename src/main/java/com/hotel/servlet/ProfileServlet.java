package com.hotel.servlet;

import com.hotel.model.AdminUser;
import com.hotel.model.PremiumGuest;
import com.hotel.model.RegularGuest;
import com.hotel.model.User;
import com.hotel.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private UserService userService = new UserService();

    // READ: show the logged-in user's details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User sessionUser = getLoggedUser(request);
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("user", userService.findById(sessionUser.getId()));
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    // UPDATE: save the changed email, password and membership type
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User sessionUser = getLoggedUser(request);
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        String type = request.getParameter("membershipType");

        if (email.isEmpty() || password.isEmpty() || email.contains(",") || password.contains(",")) {
            request.setAttribute("error", "Fields are required and must not contain commas.");
            request.setAttribute("user", userService.findById(sessionUser.getId()));
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }

        User updated;
        if (sessionUser instanceof AdminUser) {
            // admins stay admins; the membership dropdown is ignored for them
            updated = new AdminUser(sessionUser.getId(), sessionUser.getUsername(), password, email);
        } else if ("PREMIUM".equals(type)) {
            updated = new PremiumGuest(sessionUser.getId(), sessionUser.getUsername(), password, email);
        } else {
            updated = new RegularGuest(sessionUser.getId(), sessionUser.getUsername(), password, email);
        }

        userService.updateUser(updated);
        request.getSession().setAttribute("loggedUser", updated);
        request.setAttribute("success", "Profile updated.");
        request.setAttribute("user", updated);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    private User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("loggedUser");
    }
}