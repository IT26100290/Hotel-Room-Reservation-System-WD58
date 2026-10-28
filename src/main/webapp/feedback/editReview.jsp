<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Review</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <% com.hotel.model.Review rev = (com.hotel.model.Review) request.getAttribute("review"); %>
    <div class="container" style="max-width: 560px;">
        <div class="card hotel-card p-4">
            <h3 class="mb-3 text-center">Edit Review <%= rev.getId() %></h3>
            <p class="text-center text-muted">Room <%= rev.getRoomId() %> - <%= rev.getBadge() %></p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/editReview" method="post">
                <input type="hidden" name="id" value="<%= rev.getId() %>">
                <div class="mb-3">
                    <label class="form-label">Rating</label>
                    <select class="form-select" name="rating">
                        <% for (int i = 5; i >= 1; i--) { %>
                            <option value="<%= i %>" <%= i == rev.getRating() ? "selected" : "" %>><%= i %></option>
                        <% } %>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Comment (max 300 characters)</label>
                    <textarea class="form-control" name="comment" rows="4" maxlength="300" required><%= com.hotel.service.ReviewService.escapeHtml(rev.getComment()) %></textarea>
                </div>
                <button type="submit" class="btn btn-hotel w-100">Save Changes</button>
                <a class="btn btn-link w-100 mt-2" href="<%= request.getContextPath() %>/reviews">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>
