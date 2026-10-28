<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Payments</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <%
        java.util.List<com.hotel.model.Payment> payments =
            (java.util.List<com.hotel.model.Payment>) request.getAttribute("payments");
        boolean admin = (Boolean) request.getAttribute("isAdmin");
    %>
    <div class="container">
        <div class="card hotel-card p-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3 class="mb-0"><%= admin ? "All Payments" : "My Payments" %></h3>
                <a class="btn btn-hotel" href="<%= request.getContextPath() %>/pay">+ Make a Payment</a>
            </div>

            <form class="row g-2 mb-3" action="<%= request.getContextPath() %>/payments" method="get">
                <div class="col-md-6">
                    <input type="text" class="form-control" name="keyword"
                           placeholder="Search id, reservation, method or status"
                           value="<%= request.getAttribute("keyword") %>">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-hotel">Search</button>
                    <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/payments">Show all</a>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-striped align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th><th>Reservation</th>
                            <% if (admin) { %><th>Guest</th><% } %>
                            <th>Method</th><th>Amount</th><th>Fee</th><th>Total charged</th>
                            <th>Date</th><th>Status</th><th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% for (com.hotel.model.Payment p : payments) { %>
                        <tr>
                            <td><%= p.getId() %></td>
                            <td><%= p.getReservationId() %></td>
                            <% if (admin) { %><td><%= p.getUserId() %></td><% } %>
                            <td><span class="badge bg-secondary"><%= p.getType() %></span></td>
                            <td><%= String.format("%.2f", p.getAmount()) %></td>
                            <td><%= String.format("%.2f", p.calculateFee()) %></td>
                            <td><strong><%= String.format("%.2f", p.getTotalCharged()) %></strong></td>
                            <td><%= p.getDate() %></td>
                            <td><span class="badge <%= p.isPaid() ? "bg-success" : "bg-warning text-dark" %>"><%= p.getStatus() %></span></td>
                            <td class="d-flex gap-1">
                                <% if (p.isPaid()) { %>
                                    <a class="btn btn-sm btn-outline-primary"
                                       href="<%= request.getContextPath() %>/editPayment?id=<%= p.getId() %>">Change method</a>
                                <% } %>
                                <% if (admin && p.isPaid()) { %>
                                    <form action="<%= request.getContextPath() %>/refundPayment" method="post"
                                          onsubmit="return confirm('Refund this payment?');">
                                        <input type="hidden" name="id" value="<%= p.getId() %>">
                                        <button type="submit" class="btn btn-sm btn-warning">Refund</button>
                                    </form>
                                <% } %>
                                <% if (admin) { %>
                                    <form action="<%= request.getContextPath() %>/deletePayment" method="post"
                                          onsubmit="return confirm('Permanently delete this record?');">
                                        <input type="hidden" name="id" value="<%= p.getId() %>">
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                    </form>
                                <% } %>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
