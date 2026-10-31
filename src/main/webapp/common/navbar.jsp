<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // ---- who is logged in? ----
    Object navUser = session.getAttribute("loggedUser");
    boolean navAdmin = navUser instanceof com.hotel.model.AdminUser;
    String ctx = request.getContextPath();

    // ---- which page are we on? (the servlet URL the user asked for) ----
    String navPath = (String) request.getAttribute("jakarta.servlet.forward.servlet_path");
    if (navPath == null) {
        navPath = request.getServletPath();
    }
    boolean navHome = navPath.equals("") || navPath.equals("/") || navPath.equals("/index.jsp");

    // ---- page title for the banner + which menu link is highlighted ----
    String navTitle = "Hotel Reservation";
    String navGroup = "";
    if (navPath.equals("/rooms"))                   { navTitle = "Rooms";                navGroup = "rooms"; }
    else if (navPath.equals("/addRoom"))            { navTitle = "Add Room";             navGroup = "rooms"; }
    else if (navPath.equals("/editRoom"))           { navTitle = "Edit Room";            navGroup = "rooms"; }
    else if (navPath.equals("/reservations"))       { navTitle = "My Reservations";      navGroup = "reservations"; }
    else if (navPath.equals("/book"))               { navTitle = "Book a Room";          navGroup = "reservations"; }
    else if (navPath.equals("/editReservation"))    { navTitle = "Edit Reservation";     navGroup = "reservations"; }
    else if (navPath.equals("/payments"))           { navTitle = "Payments";             navGroup = "payments"; }
    else if (navPath.equals("/pay"))                { navTitle = "Make a Payment";       navGroup = "payments"; }
    else if (navPath.equals("/editPayment"))        { navTitle = "Edit Payment";         navGroup = "payments"; }
    else if (navPath.equals("/reviews"))            { navTitle = "Guest Reviews";        navGroup = "reviews"; }
    else if (navPath.equals("/addReview"))          { navTitle = "Write a Review";       navGroup = "reviews"; }
    else if (navPath.equals("/editReview"))         { navTitle = "Edit Review";          navGroup = "reviews"; }
    else if (navPath.equals("/users"))              { navTitle = "Manage Users";         navGroup = "users"; }
    else if (navPath.equals("/profile"))            { navTitle = "My Profile";           navGroup = "profile"; }
    else if (navPath.equals("/login"))              { navTitle = "Login";                navGroup = "login"; }
    else if (navPath.equals("/register"))           { navTitle = "Create an Account";    navGroup = "register"; }
%>
<nav class="site-nav" id="siteNav">
    <div class="site-nav-inner">
        <a class="site-brand" href="<%= ctx %>/index.jsp">Hotel<span>Reservation</span></a>

        <button class="site-nav-toggle" id="siteNavToggle" type="button"
                aria-label="Open menu" aria-expanded="false" aria-controls="siteNavLinks">
            <span></span><span></span><span></span>
        </button>

        <div class="site-nav-links" id="siteNavLinks">
            <a href="<%= ctx %>/index.jsp" class="<%= navHome ? "active" : "" %>">Home</a>
            <% if (navUser == null) { %>
                <a href="<%= ctx %>/login" class="<%= navGroup.equals("login") ? "active" : "" %>">Login</a>
                <a href="<%= ctx %>/register" class="<%= navGroup.equals("register") ? "active" : "" %>">Register</a>
            <% } else { %>
                <a href="<%= ctx %>/rooms" class="<%= navGroup.equals("rooms") ? "active" : "" %>">Rooms</a>
                <a href="<%= ctx %>/reservations" class="<%= navGroup.equals("reservations") ? "active" : "" %>">My Reservations</a>
                <a href="<%= ctx %>/payments" class="<%= navGroup.equals("payments") ? "active" : "" %>">Payments</a>
                <a href="<%= ctx %>/reviews" class="<%= navGroup.equals("reviews") ? "active" : "" %>">Reviews</a>
                <% if (navAdmin) { %>
                    <a href="<%= ctx %>/users" class="<%= navGroup.equals("users") ? "active" : "" %>">Manage Users</a>
                <% } %>
                <a href="<%= ctx %>/profile" class="<%= navGroup.equals("profile") ? "active" : "" %>">My Profile</a>
                <a href="<%= ctx %>/logout" class="nav-pill">Logout</a>
            <% } %>
        </div>
    </div>
</nav>
<script>
    (function () {
        var nav = document.getElementById('siteNav');
        var btn = document.getElementById('siteNavToggle');
        var links = document.getElementById('siteNavLinks');
        function onScroll() {
            if (window.scrollY > 24) { nav.classList.add('scrolled'); }
            else { nav.classList.remove('scrolled'); }
        }
        window.addEventListener('scroll', onScroll);
        onScroll();
        btn.addEventListener('click', function () {
            var open = links.classList.toggle('open');
            btn.setAttribute('aria-expanded', open ? 'true' : 'false');
        });
    })();
</script>
<% if (!navHome) { %>
<header class="page-banner">
    <div class="page-banner-inner">
        <p class="page-crumb">Home / <%= navTitle %></p>
        <h1><%= navTitle %></h1>
    </div>
</header>
<% } %>
