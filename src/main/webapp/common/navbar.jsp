<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object navUser = session.getAttribute("loggedUser");
    boolean navAdmin = navUser instanceof com.hotel.model.AdminUser;
    String ctx = request.getContextPath();
%>
<nav class="navbar navbar-expand-lg navbar-dark hotel-nav mb-4">
    <div class="container">
        <a class="navbar-brand" href="<%= ctx %>/index.jsp">Hotel Reservation</a>
        <div class="navbar-nav ms-auto">
            <% if (navUser == null) { %>
                <a class="nav-link" href="<%= ctx %>/login">Login</a>
                <a class="nav-link" href="<%= ctx %>/register">Register</a>
            <% } else { %>
                <a class="nav-link" href="<%= ctx %>/profile">My Profile</a>
                <a class="nav-link" href="<%= ctx %>/reservations">My Reservations</a>
                <a class="nav-link" href="<%= ctx %>/rooms">Rooms</a>
                <% if (navAdmin) { %>
                    <a class="nav-link" href="<%= ctx %>/users">Manage Users</a>
                <% } %>
                <a class="nav-link" href="<%= ctx %>/logout">Logout</a>
            <% } %>
        </div>
    </div>
</nav>