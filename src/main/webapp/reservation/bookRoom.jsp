<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Book a Room</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <%
        java.util.List<com.hotel.model.Room> rooms =
            (java.util.List<com.hotel.model.Room>) request.getAttribute("rooms");
        String selected = (String) request.getAttribute("selectedRoomId");
    %>
    <div class="container" style="max-width: 560px;">
        <div class="card hotel-card p-4">
            <h3 class="mb-3 text-center">Book a Room</h3>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/book" method="post">
                <div class="mb-3">
                    <label class="form-label">Room</label>
                    <select class="form-select" name="roomId" required>
                        <% for (com.hotel.model.Room room : rooms) { %>
                            <option value="<%= room.getId() %>" <%= room.getId().equals(selected) ? "selected" : "" %>>
                                <%= room.getRoomNumber() %> - <%= room.getType() %> -
                                <%= String.format("%.2f", room.getPricePerNight()) %> / night
                            </option>
                        <% } %>
                    </select>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Check-in</label>
                        <input type="date" class="form-control" name="checkIn" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Check-out</label>
                        <input type="date" class="form-control" name="checkOut" required>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label">Booking type</label>
                    <select class="form-select" name="type">
                        <option value="STANDARD">Standard (20% cancellation fee)</option>
                        <option value="FLEXIBLE">Flexible (+5% price, free cancellation)</option>
                        <option value="NON_REFUNDABLE">Non-refundable (10% cheaper, no refund)</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-hotel w-100">Confirm Booking</button>
                <a class="btn btn-link w-100 mt-2" href="<%= request.getContextPath() %>/reservations">Back to my reservations</a>
            </form>
        </div>
    </div>
</body>
</html>