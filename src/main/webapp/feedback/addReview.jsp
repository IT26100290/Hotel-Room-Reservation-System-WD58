<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Write a Review</title>
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
            <h3 class="mb-3 text-center">Write a Review</h3>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/addReview" method="post">
                <div class="mb-3">
                    <label class="form-label">Room</label>
                    <select class="form-select" name="roomId" required>
                        <% for (com.hotel.model.Room room : rooms) { %>
                            <option value="<%= room.getId() %>" <%= room.getId().equals(selected) ? "selected" : "" %>>
                                Room <%= room.getRoomNumber() %> - <%= room.getType() %>
                            </option>
                        <% } %>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Rating</label>
                    <select class="form-select" name="rating">
                        <option value="5">5 - Excellent</option>
                        <option value="4">4 - Good</option>
                        <option value="3">3 - Average</option>
                        <option value="2">2 - Poor</option>
                        <option value="1">1 - Terrible</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Comment (max 300 characters)</label>
                    <textarea class="form-control" name="comment" rows="4" maxlength="300" required></textarea>
                </div>
                <p class="text-muted small">If you have a booking for this room, your review is marked as a verified stay.</p>
                <button type="submit" class="btn btn-hotel w-100">Submit Review</button>
                <a class="btn btn-link w-100 mt-2" href="<%= request.getContextPath() %>/reviews">Back to reviews</a>
            </form>
        </div>
    </div>
</body>
</html>
