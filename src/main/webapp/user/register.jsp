<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <h2>User Registration</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <% if (request.getAttribute("success") != null) { %>
        <p style="color:green;"><%= request.getAttribute("success") %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/register" method="post">
        <p>Username: <input type="text" name="username" required></p>
        <p>Password: <input type="password" name="password" required></p>
        <p>Email: <input type="email" name="email" required></p>
        <p>Membership:
            <select name="membershipType">
                <option value="REGULAR">Regular</option>
                <option value="PREMIUM">Premium</option>
            </select>
        </p>
        <button type="submit">Register</button>
    </form>
</body>
</html>