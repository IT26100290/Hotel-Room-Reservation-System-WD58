package com.hotel.servlet;

import com.hotel.model.PremiumGuest;
import com.hotel.model.RegularGuest;
import com.hotel.model.User;
import com.hotel.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService = new UserService();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String email = request.getParameter("email").trim();
        String type = request.getParameter("membershipType");


        if (username.isEmpty() || password.isEmpty() || email.isEmpty()
                || username.contains(",") || password.contains(",") || email.contains(",")) {
            request.setAttribute("error", "All fields are required and must not contain commas.");
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
            return;
        }

        if (userService.findByUsername(username) != null) {
            request.setAttribute("error", "That username is already taken.");
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
            return;
        }

        String id = userService.generateNextId();
        User user;
        if ("PREMIUM".equals(type)) {
            user = new PremiumGuest(id, username, password, email);   // polymorphism:
        } else {
            user = new RegularGuest(id, username, password, email);   // both are Users
        }

        userService.addUser(user);
        request.setAttribute("success", "Registered successfully. Your ID is " + id);
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }
}