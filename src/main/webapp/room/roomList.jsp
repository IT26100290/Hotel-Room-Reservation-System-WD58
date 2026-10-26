<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Rooms</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <div class="container">
        <div class="card hotel-card p-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3 class="mb-0">Rooms</h3>
                <a class="btn btn-hotel" href="<%= request.getContextPath() %>/addRoom">+ Add Room</a>
            </div>

            <form class="row g-2 mb-3" action="<%= request.getContextPath() %>/rooms" method="get">
                <div class="col-md-6">
                    <input type="text" class="form-control" name="keyword"
                           placeholder="Search id, room number, type or status"
                           value="<%= request.getAttribute("keyword") %>">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-hotel">Search</button>
                    <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/rooms">Show all</a>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-striped align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th><th>Room No</th><th>Type</th><th>Price / night</th>
                            <th>1 night total</th><th>Status</th><th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        java.util.List<com.hotel.model.Room> rooms =
                            (java.util.List<com.hotel.model.Room>) request.getAttribute("rooms");
                        for (com.hotel.model.Room r : rooms) {
                    %>
                        <tr>
                            <td><%= r.getId() %></td>
                            <td><%= r.getRoomNumber() %></td>
                            <td><span class="badge bg-secondary"><%= r.getType() %></span></td>
                            <td><%= String.format("%.2f", r.getPricePerNight()) %></td>
                            <td><%= String.format("%.2f", r.calculateTotal(1)) %></td>
                            <td><%= r.getStatus() %></td>
                            <td class="d-flex gap-1">
                                <a class="btn btn-sm btn-outline-primary"
                                   href="<%= request.getContextPath() %>/editRoom?id=<%= r.getId() %>">Edit</a>
                                <form action="<%= request.getContextPath() %>/deleteRoom" method="post"
                                      onsubmit="return confirm('Delete this room?');">
                                    <input type="hidden" name="id" value="<%= r.getId() %>">
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