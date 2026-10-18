<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hotel Room Reservation System</title>
</head>
<body>
    <h1>Hotel Room Reservation System</h1>
    <ul>
        <li><a href="<%= request.getContextPath() %>/register">Register</a></li>
        <li><a href="<%= request.getContextPath() %>/login">Login</a></li>
        <li><a href="<%= request.getContextPath() %>/profile">My Profile</a></li>
        <li><a href="<%= request.getContextPath() %>/users">User List (Admin)</a></li>
    </ul>
</body>
</html>