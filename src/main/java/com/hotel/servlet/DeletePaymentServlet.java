package com.hotel.servlet;

import com.hotel.model.User;
import com.hotel.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/deletePayment")
public class DeletePaymentServlet extends HttpServlet {

    private PaymentService paymentService = new PaymentService();

    // DELETE: remove the record completely. Admin only.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loggedUser");
        if (user == null || !paymentService.isAdmin(user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String id = request.getParameter("id");
        if (id != null) {
            paymentService.deletePayment(id);
        }
        response.sendRedirect(request.getContextPath() + "/payments");
    }
}
