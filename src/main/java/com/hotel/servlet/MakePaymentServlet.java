package com.hotel.servlet;

import com.hotel.model.Payment;
import com.hotel.model.Reservation;
import com.hotel.model.User;
import com.hotel.service.PaymentService;
import com.hotel.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/pay")
public class MakePaymentServlet extends HttpServlet {

    private PaymentService paymentService = new PaymentService();
    private ReservationService reservationService = new ReservationService();

    // GET /pay  or  /pay?reservationId=B001 : show the payment form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        showForm(request, response, user, request.getParameter("reservationId"));
    }

    // POST: validate and save the payment (CREATE)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reservationId = request.getParameter("reservationId");
        String method = request.getParameter("method");

        String error = paymentService.validate(reservationId, user);
        if (error != null) {
            request.setAttribute("error", error);
            showForm(request, response, user, reservationId);
            return;
        }

        // the amount always comes from the reservation, never from the form
        Reservation r = reservationService.findById(reservationId);
        Payment payment = PaymentService.createPayment(paymentService.generateNextId(),
                reservationId, r.getUserId(), method, r.getTotalPrice(), LocalDate.now(), "PAID");
        paymentService.addPayment(payment);
        response.sendRedirect(request.getContextPath() + "/payments");
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response,
                          User user, String selected) throws ServletException, IOException {
        request.setAttribute("payable", paymentService.getPayableReservations(user));
        request.setAttribute("selectedReservationId", selected == null ? "" : selected);
        request.getRequestDispatcher("/payment/makePayment.jsp").forward(request, response);
    }

    private User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (User) session.getAttribute("loggedUser");
    }
}
