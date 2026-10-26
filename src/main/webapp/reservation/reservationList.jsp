<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reservations</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <%
        java.util.List<com.hotel.model.Reservation> reservations =
            (java.util.List<com.hotel.model.Reservation>) request.getAttribute("reservations");
        java.util.Map<String, String> roomNumbers =
            (java.util.Map<String, String>) request.getAttribute("roomNumbers");
        boolean admin = (Boolean) request.getAttribute("isAdmin");
    %>
    <div class="container">
        <div class="card hotel-card p-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3 class="mb-0"><%= admin ? "All Reservations" : "My Reservations" %></h3>
                <a class="btn btn-hotel" href="<%= request.getContextPath() %>/book">+ Book a Room</a>
            </div>

            <form class="row g-2 mb-3" action="<%= request.getContextPath() %>/reservations" method="get">
                <div class="col-md-6">
                    <input type="text" class="form-control" name="keyword"
                           placeholder="Search id, room, type or status"
                           value="<%= request.getAttribute("keyword") %>">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-hotel">Search</button>
                    <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/reservations">Show all</a>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-striped align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <% if (admin) { %><th>Guest</th><% } %>
                            <th>Room</th><th>Check-in</th><th>Check-out</th><th>Nights</th>
                            <th>Type</th><th>Total</th><th>Status</th><th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% for (com.hotel.model.Reservation r : reservations) { %>
                        <tr>
                            <td><%= r.getId() %></td>
                            <% if (admin) { %><td><%= r.getUserId() %></td><% } %>
                            <td><%= roomNumbers.containsKey(r.getRoomId()) ? roomNumbers.get(r.getRoomId()) : r.getRoomId() %></td>
                            <td><%= r.getCheckIn() %></td>
                            <td><%= r.getCheckOut() %></td>
                            <td><%= r.getNights() %></td>
                            <td><span class="badge bg-secondary"><%= r.getType() %></span></td>
                            <td><%= String.format("%.2f", r.getTotalPrice()) %></td>
                            <td>
                                <span class="badge <%= r.isActive() ? "bg-success" : "bg-danger" %>"><%= r.getStatus() %></span>
                            </td>
                            <td class="d-flex gap-1">
                                <% if (r.isActive()) { %>
                                    <a class="btn btn-sm btn-outline-primary"
                                       href="<%= request.getContextPath() %>/editReservation?id=<%= r.getId() %>">Modify</a>
                                    <form action="<%= request.getContextPath() %>/cancelReservation" method="post"
                                          onsubmit="return confirm('Cancel this reservation? Cancellation fee: <%= String.format("%.2f", r.calculateCancellationFee()) %>');">
                                        <input type="hidden" name="id" value="<%= r.getId() %>">
                                        <button type="submit" class="btn btn-sm btn-warning">Cancel</button>
                                    </form>
                                <% } %>
                                <% if (admin) { %>
                                    <form action="<%= request.getContextPath() %>/deleteReservation" method="post"
                                          onsubmit="return confirm('Permanently delete this record?');">
                                        <input type="hidden" name="id" value="<%= r.getId() %>">
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