<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reviews</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <%
        java.util.List<com.hotel.model.Review> reviews =
            (java.util.List<com.hotel.model.Review>) request.getAttribute("reviews");
        java.util.List<com.hotel.model.Room> rooms =
            (java.util.List<com.hotel.model.Room>) request.getAttribute("rooms");
        java.util.Map<String, String> roomNumbers =
            (java.util.Map<String, String>) request.getAttribute("roomNumbers");
        String selected = (String) request.getAttribute("selectedRoomId");
        String currentUserId = (String) request.getAttribute("currentUserId");
        boolean admin = (Boolean) request.getAttribute("isAdmin");
        double average = (Double) request.getAttribute("average");
    %>
    <div class="container">
        <div class="card hotel-card p-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3 class="mb-0">Guest Reviews</h3>
                <a class="btn btn-hotel" href="<%= request.getContextPath() %>/addReview<%= selected.isEmpty() ? "" : "?roomId=" + selected %>">+ Write a Review</a>
            </div>

            <form class="row g-2 mb-3" action="<%= request.getContextPath() %>/reviews" method="get">
                <div class="col-md-4">
                    <select class="form-select" name="roomId">
                        <option value="">All rooms</option>
                        <% for (com.hotel.model.Room room : rooms) { %>
                            <option value="<%= room.getId() %>" <%= room.getId().equals(selected) ? "selected" : "" %>>
                                Room <%= room.getRoomNumber() %> (<%= room.getType() %>)
                            </option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-5">
                    <input type="text" class="form-control" name="keyword"
                           placeholder="Search comments, type or id"
                           value="<%= com.hotel.service.ReviewService.escapeHtml((String) request.getAttribute("keyword")) %>">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-hotel">Filter</button>
                    <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/reviews">Show all</a>
                </div>
            </form>

            <p class="mb-3">
                <strong>Average rating:</strong>
                <%= reviews.isEmpty() ? "No reviews yet" : String.format("%.1f", average) + " / 5" %>
                <span class="text-muted">(<%= reviews.size() %> reviews, verified stays count double)</span>
            </p>

            <% for (com.hotel.model.Review r : reviews) { %>
                <div class="border rounded p-3 mb-3">
                    <div class="d-flex justify-content-between">
                        <div>
                            <span class="text-warning fs-5">
                                <% for (int i = 1; i <= 5; i++) { %><%= i <= r.getRating() ? "&#9733;" : "&#9734;" %><% } %>
                            </span>
                            <span class="badge <%= "VERIFIED".equals(r.getType()) ? "bg-success" : "bg-secondary" %> ms-2"><%= r.getBadge() %></span>
                        </div>
                        <small class="text-muted">
                            <%= r.getId() %> &middot; Room <%= roomNumbers.containsKey(r.getRoomId()) ? roomNumbers.get(r.getRoomId()) : r.getRoomId() %>
                            &middot; <%= r.getUserId() %> &middot; <%= r.getDate() %>
                        </small>
                    </div>
                    <p class="mb-2 mt-2"><%= com.hotel.service.ReviewService.escapeHtml(r.getComment()) %></p>
                    <% if (admin || r.getUserId().equals(currentUserId)) { %>
                        <div class="d-flex gap-1">
                            <a class="btn btn-sm btn-outline-primary"
                               href="<%= request.getContextPath() %>/editReview?id=<%= r.getId() %>">Edit</a>
                            <form action="<%= request.getContextPath() %>/deleteReview" method="post"
                                  onsubmit="return confirm('Delete this review?');">
                                <input type="hidden" name="id" value="<%= r.getId() %>">
                                <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                            </form>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
