<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Modify Reservation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <% com.hotel.model.Reservation res = (com.hotel.model.Reservation) request.getAttribute("reservation"); %>
    <div class="container" style="max-width: 560px;">
        <div class="card hotel-card p-4">
            <h3 class="mb-3 text-center">Modify Reservation <%= res.getId() %></h3>
            <p class="text-center text-muted">Room <%= res.getRoomId() %></p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/editReservation" method="post">
                <input type="hidden" name="id" value="<%= res.getId() %>">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Check-in</label>
                        <input type="date" class="form-control" name="checkIn" value="<%= res.getCheckIn() %>" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Check-out</label>
                        <input type="date" class="form-control" name="checkOut" value="<%= res.getCheckOut() %>" required>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label">Booking type</label>
                    <select class="form-select" name="type">
                        <option value="STANDARD" <%= "STANDARD".equals(res.getType()) ? "selected" : "" %>>Standard (20% cancellation fee)</option>
                        <option value="FLEXIBLE" <%= "FLEXIBLE".equals(res.getType()) ? "selected" : "" %>>Flexible (+5% price, free cancellation)</option>
                        <option value="NON_REFUNDABLE" <%= "NON_REFUNDABLE".equals(res.getType()) ? "selected" : "" %>>Non-refundable (10% cheaper, no refund)</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-hotel w-100">Save Changes</button>
                <a class="btn btn-link w-100 mt-2" href="<%= request.getContextPath() %>/reservations">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>