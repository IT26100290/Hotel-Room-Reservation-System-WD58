<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Make a Payment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <%
        java.util.List<com.hotel.model.Reservation> payable =
            (java.util.List<com.hotel.model.Reservation>) request.getAttribute("payable");
        String selected = (String) request.getAttribute("selectedReservationId");
    %>
    <div class="container" style="max-width: 560px;">
        <div class="card hotel-card p-4">
            <h3 class="mb-3 text-center">Make a Payment</h3>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>

            <% if (payable.isEmpty()) { %>
                <div class="alert alert-info">You have no unpaid reservations.</div>
                <a class="btn btn-hotel w-100" href="<%= request.getContextPath() %>/book">Book a room</a>
            <% } else { %>
            <form action="<%= request.getContextPath() %>/pay" method="post">
                <div class="mb-3">
                    <label class="form-label">Reservation</label>
                    <select class="form-select" name="reservationId" required>
                        <% for (com.hotel.model.Reservation r : payable) { %>
                            <option value="<%= r.getId() %>" <%= r.getId().equals(selected) ? "selected" : "" %>>
                                <%= r.getId() %> - Room <%= r.getRoomId() %> -
                                <%= r.getCheckIn() %> to <%= r.getCheckOut() %> -
                                <%= String.format("%.2f", r.getTotalPrice()) %>
                            </option>
                        <% } %>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Payment method</label>
                    <select class="form-select" name="method">
                        <option value="CARD">Card (2.5% fee)</option>
                        <option value="CASH">Cash (no fee)</option>
                        <option value="BANK_TRANSFER">Bank transfer (1% fee)</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-hotel w-100">Pay Now</button>
            </form>
            <% } %>
            <a class="btn btn-link w-100 mt-2" href="<%= request.getContextPath() %>/payments">Back to my payments</a>
        </div>
    </div>
</body>
</html>
