<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.hotel.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>User List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <div class="container">
        <div class="card hotel-card p-4">
            <h3 class="mb-3">All Users</h3>

            <form class="row g-2 mb-3" action="<%= request.getContextPath() %>/users" method="get">
                <div class="col-md-6">
                    <input type="text" class="form-control" name="keyword"
                           placeholder="Search id, username or email"
                           value="<%= request.getAttribute("keyword") %>">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-hotel">Search</button>
                    <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/users">Show all</a>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-striped align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th><th>Username</th><th>Email</th>
                            <th>Membership</th><th>Discount</th><th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        List<User> users = (List<User>) request.getAttribute("users");
                        for (User u : users) {
                    %>
                        <tr>
                            <td><%= u.getId() %></td>
                            <td><%= u.getUsername() %></td>
                            <td><%= u.getEmail() %></td>
                            <td><span class="badge bg-secondary"><%= u.getMembershipType() %></span></td>
                            <td><%= u.getDiscountRate() * 100 %>%</td>
                            <td>
                                <form action="<%= request.getContextPath() %>/deleteUser" method="post"
                                      onsubmit="return confirm('Delete this user?');">
                                    <input type="hidden" name="id" value="<%= u.getId() %>">
                                    <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>