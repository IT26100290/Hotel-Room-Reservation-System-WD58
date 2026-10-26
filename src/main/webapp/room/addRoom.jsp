<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Add Room</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <div class="container" style="max-width: 520px;">
        <div class="card hotel-card p-4">
            <h3 class="mb-3 text-center">Add Room</h3>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/addRoom" method="post">
                <div class="mb-3">
                    <label class="form-label">Room number</label>
                    <input type="text" class="form-control" name="roomNumber" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Type</label>
                    <select class="form-select" name="type">
                        <option value="STANDARD">Standard</option>
                        <option value="DELUXE">Deluxe</option>
                        <option value="SUITE">Suite</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Price per night</label>
                    <input type="number" step="0.01" min="0.01" class="form-control" name="pricePerNight" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Status</label>
                    <select class="form-select" name="status">
                        <option value="AVAILABLE">Available</option>
                        <option value="BOOKED">Booked</option>
                        <option value="MAINTENANCE">Maintenance</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-hotel w-100">Save Room</button>
                <a class="btn btn-link w-100 mt-2" href="<%= request.getContextPath() %>/rooms">Back to list</a>
            </form>
        </div>
    </div>
</body>
</html>