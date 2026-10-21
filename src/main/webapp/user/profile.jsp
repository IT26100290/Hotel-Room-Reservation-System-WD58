<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotel.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>
    <% User user = (User) request.getAttribute("user"); %>

    <div class="container" style="max-width: 560px;">
        <div class="card hotel-card p-4">
            <h3 class="mb-3">My Profile</h3>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getAttribute("success") != null) { %>
                <div class="alert alert-success"><%= request.getAttribute("success") %></div>
            <% } %>

            <ul class="list-group list-group-flush mb-3">
                <li class="list-group-item"><strong>ID:</strong> <%= user.getId() %></li>
                <li class="list-group-item"><strong>Username:</strong> <%= user.getUsername() %></li>
                <li class="list-group-item"><strong>Discount:</strong> <%= user.getDiscountRate() * 100 %>%</li>
            </ul>

            <form action="<%= request.getContextPath() %>/profile" method="post">
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" name="email" value="<%= user.getEmail() %>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="text" class="form-control" name="password" value="<%= user.getPassword() %>" required>
                </div>

                <% if ("ADMIN".equals(user.getMembershipType())) { %>
                    <p><strong>Membership:</strong> <span class="badge bg-dark">ADMIN</span></p>
                <% } else { %>
                    <div class="mb-3">
                        <label class="form-label">Membership</label>
                        <select class="form-select" name="membershipType">
                            <option value="REGULAR" <%= "REGULAR".equals(user.getMembershipType()) ? "selected" : "" %>>Regular</option>
                            <option value="PREMIUM" <%= "PREMIUM".equals(user.getMembershipType()) ? "selected" : "" %>>Premium</option>
                        </select>
                    </div>
                <% } %>

                <button type="submit" class="btn btn-hotel w-100">Update</button>
            </form>
        </div>
    </div>
</body>
</html>