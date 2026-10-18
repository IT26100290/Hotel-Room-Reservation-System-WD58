<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotel.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile</title>
</head>
<body>
    <% User user = (User) request.getAttribute("user"); %>

    <h2>My Profile</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>

    <p>ID: <%= user.getId() %></p>
    <p>Username: <%= user.getUsername() %></p>
    <p>Discount: <%= user.getDiscountRate() * 100 %>%</p>

    <form action="<%= request.getContextPath() %>/profile" method="post">
        <p>Email: <input type="email" name="email" value="<%= user.getEmail() %>" required></p>
        <p>Password: <input type="text" name="password" value="<%= user.getPassword() %>" required></p>

        <% if ("ADMIN".equals(user.getMembershipType())) { %>
            <p>Membership: ADMIN</p>
        <% } else { %>
            <p>Membership:
                <select name="membershipType">
                    <option value="REGULAR" <%= "REGULAR".equals(user.getMembershipType()) ? "selected" : "" %>>Regular</option>
                    <option value="PREMIUM" <%= "PREMIUM".equals(user.getMembershipType()) ? "selected" : "" %>>Premium</option>
                </select>
            </p>
        <% } %>

        <button type="submit">Update</button>
    </form>

    <p><a href="<%= request.getContextPath() %>/logout">Logout</a></p>
</body>
</html>