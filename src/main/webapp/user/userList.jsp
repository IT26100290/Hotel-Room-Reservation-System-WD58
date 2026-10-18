<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.hotel.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
</head>
<body>
    <h2>All Users</h2>

    <form action="<%= request.getContextPath() %>/users" method="get">
        <input type="text" name="keyword" placeholder="Search id, username or email"
               value="<%= request.getAttribute("keyword") %>">
        <button type="submit">Search</button>
        <a href="<%= request.getContextPath() %>/users">Show all</a>
    </form>

    <table border="1" cellpadding="6">
        <tr>
            <th>ID</th><th>Username</th><th>Email</th><th>Membership</th><th>Discount</th><th>Action</th>
        </tr>
        <%
            List<User> users = (List<User>) request.getAttribute("users");
            for (User u : users) {
        %>
        <tr>
            <td><%= u.getId() %></td>
            <td><%= u.getUsername() %></td>
            <td><%= u.getEmail() %></td>
            <td><%= u.getMembershipType() %></td>
            <td><%= u.getDiscountRate() * 100 %>%</td>
            <td>
                <form action="<%= request.getContextPath() %>/deleteUser" method="post"
                      onsubmit="return confirm('Delete this user?');">
                    <input type="hidden" name="id" value="<%= u.getId() %>">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
</body>
</html>