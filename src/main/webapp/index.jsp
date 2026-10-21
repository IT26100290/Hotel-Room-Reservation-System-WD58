<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hotel Room Reservation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <div class="container">
        <div class="hotel-hero text-center">
            <h1 class="display-5 fw-bold">Hotel Room Reservation System</h1>
            <p class="lead">Find a room, book your stay, manage your account.</p>
            <% if (session.getAttribute("loggedUser") == null) { %>
                <a class="btn btn-hotel btn-lg me-2" href="<%= request.getContextPath() %>/register">Register</a>
                <a class="btn btn-outline-light btn-lg" href="<%= request.getContextPath() %>/login">Login</a>
            <% } else { %>
                <a class="btn btn-hotel btn-lg" href="<%= request.getContextPath() %>/profile">Go to my profile</a>
            <% } %>
        </div>
    </div>
</body>
</html>