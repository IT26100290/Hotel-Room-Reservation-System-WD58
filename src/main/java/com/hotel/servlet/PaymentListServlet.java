package com.hotel.servlet;

import com.hotel.model.Payment;
import com.hotel.model.User;
import com.hotel.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/payments")
public class PaymentListServlet extends HttpServlet {

    private PaymentService paymentService = new PaymentService();

    // READ: guests see their own payments, admins see all
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
        List<Payment> payments = paymentService.searchPayments(keyword, user);

        request.setAttribute("payments", payments);
        request.setAttribute("isAdmin", paymentService.isAdmin(user));
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.getRequestDispatcher("/payment/paymentList.jsp").forward(request, response);
    }
}
