<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hotel Reservation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
    <script>document.documentElement.classList.add('js');</script>
</head>
<body>
    <%@ include file="/common/navbar.jsp" %>

    <!-- HERO -->
    <header class="home-hero" id="top">
        <div class="home-hero-inner">
            <p class="eyebrow">A stay worth booking</p>
            <h1 class="home-title">Find your room.<br>Book in minutes.</h1>
            <p class="home-lead">Pools, palms and sea air. Pick a room, choose your dates and pay your way.</p>
            <div class="hero-actions">
                <a class="btn-sand" href="<%= ctx %>/rooms">Browse rooms</a>
                <a class="btn-ghost" href="#about">Learn more</a>
            </div>
        </div>
    </header>

    <!-- QUICK SEARCH (sends the visitor to the room list) -->
    <section class="search-wrap">
        <form class="search-bar" action="<%= ctx %>/rooms" method="get">
            <div class="search-field">
                <label for="typeSelect">Room type</label>
                <select id="typeSelect">
                    <option value="">All rooms</option>
                    <option value="Standard">Standard</option>
                    <option value="Deluxe">Deluxe</option>
                    <option value="Suite">Suite</option>
                </select>
            </div>
            <div class="search-field">
                <label for="keyword">Search</label>
                <input id="keyword" name="keyword" type="text" placeholder="Room number, type or status">
            </div>
            <button class="search-btn" type="submit">Find a room</button>
        </form>
    </section>

    <!-- ABOUT -->
    <section class="section" id="about">
        <div class="section-inner about-grid">
            <div class="about-photo reveal">
                <div class="arch-img">
                    <img src="<%= ctx %>/images/about.jpg" alt="Hotel lobby opening to the sea">
                </div>
            </div>
            <div class="about-text reveal">
                <p class="eyebrow dark">About us</p>
                <h2>Stay with comfort, close to the water</h2>
                <p>Light rooms, a calm lobby and a pool a few steps away. Book the room that suits your trip and manage your stay online, from reservation to payment to review.</p>
                <div class="mini-grid">
                    <div class="mini-card"><strong>Easy booking</strong><span>See the total before you confirm.</span></div>
                    <div class="mini-card"><strong>Pay your way</strong><span>Card, cash or bank transfer.</span></div>
                </div>
            </div>
        </div>
    </section>

    <!-- ROOMS -->
    <section class="section" id="rooms">
        <div class="section-inner">
            <div class="section-head reveal">
                <p class="eyebrow dark">Our rooms</p>
                <h2>Rooms &amp; suites</h2>
            </div>
            <div class="room-grid">
                <article class="room-card reveal">
                    <div class="room-photo">
                        <img src="<%= ctx %>/images/room-standard.jpg" alt="Standard room with a white bed">
                        <span class="room-tag">Standard</span>
                    </div>
                    <div class="room-body">
                        <h3>Standard Room</h3>
                        <p class="grow">A bright, simple room with a comfortable bed and a window.</p>
                        <div class="room-meta">
                            <span class="room-price"><strong>Rs. 8,000</strong> / night</span>
                            <a class="btn-book" href="<%= ctx %>/rooms?keyword=Standard">View rooms</a>
                        </div>
                    </div>
                </article>
                <article class="room-card reveal">
                    <div class="room-photo">
                        <img src="<%= ctx %>/images/room-deluxe.jpg" alt="Deluxe room with wood panelling">
                        <span class="room-tag">Deluxe</span>
                    </div>
                    <div class="room-body">
                        <h3>Deluxe Room</h3>
                        <p class="grow">More space, warm wood and big windows with a view.</p>
                        <div class="room-meta">
                            <span class="room-price"><strong>Rs. 15,000</strong> / night</span>
                            <a class="btn-book" href="<%= ctx %>/rooms?keyword=Deluxe">View rooms</a>
                        </div>
                    </div>
                </article>
                <article class="room-card reveal">
                    <div class="room-photo">
                        <img src="<%= ctx %>/images/room-suite.jpg" alt="Suite with a skylight and balcony door">
                        <span class="room-tag sand">Suite</span>
                    </div>
                    <div class="room-body">
                        <h3>Panorama Suite</h3>
                        <p class="grow">Our largest room, with a skylight, a balcony door and a view.</p>
                        <div class="room-meta">
                            <span class="room-price"><strong>Rs. 28,000</strong> / night</span>
                            <a class="btn-book" href="<%= ctx %>/rooms?keyword=Suite">View rooms</a>
                        </div>
                    </div>
                </article>
            </div>
        </div>
    </section>

    <!-- AMENITIES -->
    <section class="section" id="amenities">
        <div class="section-inner">
            <div class="section-head reveal">
                <p class="eyebrow dark">Around the hotel</p>
                <h2>Pool, dining &amp; spa</h2>
            </div>
            <div class="gallery">
                <div class="tile reveal">
                    <img src="<%= ctx %>/images/amenity-pool.jpg" alt="Resort pool and beach from above">
                    <div class="tile-label">Pool &amp; beach</div>
                </div>
                <div class="tile reveal">
                    <img src="<%= ctx %>/images/amenity-dining.jpg" alt="Restaurant dining room">
                    <div class="tile-label">Restaurant</div>
                </div>
                <div class="tile reveal">
                    <img src="<%= ctx %>/images/amenity-spa.jpg" alt="Spa room with a stone bath">
                    <div class="tile-label">Spa &amp; wellness</div>
                </div>
            </div>
        </div>
    </section>

    <!-- SHARE YOUR STAY -->
    <section class="section" id="reviews">
        <div class="section-inner">
            <div class="share-box reveal">
                <div class="share-head">
                    <div>
                        <p class="eyebrow dark">Guest reviews</p>
                        <h2>Share your stay</h2>
                    </div>
                    <a class="link-arrow" href="<%= ctx %>/reviews">Read all reviews</a>
                </div>
                <div class="share-grid">
                    <div class="share-card"><div class="stars" aria-hidden="true">&#9733;&#9733;&#9733;&#9733;&#9733;</div><strong>Rate your room</strong><p>Give your stay one to five stars and tell other guests what it was like.</p></div>
                    <div class="share-card"><div class="stars" aria-hidden="true">&#9733;&#9733;&#9733;&#9733;&#9734;</div><strong>Verified stays count double</strong><p>Reviews from guests who really stayed carry more weight in the average.</p></div>
                    <div class="share-card"><div class="stars" aria-hidden="true">&#9733;&#9733;&#9733;&#9733;&#9733;</div><strong>Edit any time</strong><p>Change or remove your review whenever you like.</p></div>
                </div>
            </div>
        </div>
    </section>

    <!-- CALL TO ACTION -->
    <section class="section">
        <div class="cta-band reveal">
            <div>
                <h2>Ready for your stay?</h2>
                <p>Find a room that fits your dates and book it in minutes.</p>
            </div>
            <a class="btn-sand" href="<%= ctx %>/rooms">Browse rooms</a>
        </div>
    </section>

    <!-- FOOTER -->
    <footer class="site-footer">
        <div class="site-footer-inner">
            <strong>Hotel<span>Reservation</span></strong>
            <span>SE1020 Object Oriented Programming &middot; Group project</span>
        </div>
    </footer>

    <script>
        // choosing a room type fills the search box
        var typeSelect = document.getElementById('typeSelect');
        var keyword = document.getElementById('keyword');
        typeSelect.addEventListener('change', function () { keyword.value = typeSelect.value; });

        // fade sections in as they scroll into view
        var items = document.querySelectorAll('.reveal');
        if ('IntersectionObserver' in window) {
            var io = new IntersectionObserver(function (entries) {
                entries.forEach(function (e) {
                    if (e.isIntersecting) { e.target.classList.add('in-view'); io.unobserve(e.target); }
                });
            }, { threshold: 0.12 });
            items.forEach(function (el) { io.observe(el); });
        } else {
            items.forEach(function (el) { el.classList.add('in-view'); });
        }
    </script>
</body>
</html>
