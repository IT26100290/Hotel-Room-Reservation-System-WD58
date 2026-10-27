<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Change Payment Method</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <% com.hotel.model.Payment pay = (com.hotel.model.Payment) request.getAttribute("payment"); %>
    <div class="container" style="max-width: 520px;">
        <div class="card hotel-card p-4">
            <h3 class="mb-3 text-center">Change Method for <%= pay.getId() %></h3>
            <p class="text-center text-muted">
                Reservation <%= pay.getReservationId() %> - amount <%= String.format("%.2f", pay.getAmount()) %>
            </p>

            <form action="<%= request.getContextPath() %>/editPayment" method="post">
                <input type="hidden" name="id" value="<%= pay.getId() %>">
                <div class="mb-3">
                    <label class="form-label">Payment method</label>
                    <select class="form-select" name="method">
                        <option value="CARD" <%= "CARD".equals(pay.getType()) ? "selected" : "" %>>Card (2.5% fee)</option>
                        <option value="CASH" <%= "CASH".equals(pay.getType()) ? "selected" : "" %>>Cash (no fee)</option>
                        <option value="BANK_TRANSFER" <%= "BANK_TRANSFER".equals(pay.getType()) ? "selected" : "" %>>Bank transfer (1% fee)</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-hotel w-100">Save Method</button>
                <a class="btn btn-link w-100 mt-2" href="<%= request.getContextPath() %>/payments">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>
