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

@WebServlet("/editPayment")
public class EditPaymentServlet extends HttpServlet {

    private PaymentService paymentService = new PaymentService();

    // GET /editPayment?id=P001 : show the form to change the payment method
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        Payment p = (user == null) ? null : paymentService.findById(request.getParameter("id"));
        if (user == null || p == null || !paymentService.canAccess(user, p) || !p.isPaid()) {
            response.sendRedirect(request.getContextPath() + "/payments");
            return;
        }
        request.setAttribute("payment", p);
        request.getRequestDispatcher("/payment/editPayment.jsp").forward(request, response);
    }

    // POST: change the payment method (UPDATE). The subclass changes, the amount stays.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        Payment existing = (user == null) ? null : paymentService.findById(request.getParameter("id"));
        if (user == null || existing == null || !paymentService.canAccess(user, existing)
                || !existing.isPaid()) {
            response.sendRedirect(request.getContextPath() + "/payments");
            return;
        }

        String method = request.getParameter("method");
        paymentService.updatePayment(PaymentService.createPayment(existing.getId(),
                existing.getReservationId(), existing.getUserId(), method,
                existing.getAmount(), existing.getDate(), existing.getStatus()));
        response.sendRedirect(request.getContextPath() + "/payments");
    }

    private User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (User) session.getAttribute("loggedUser");
    }
}
