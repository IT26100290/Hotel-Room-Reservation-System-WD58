FEEDBACK / REVIEWS COMPONENT - HOW TO ADD IT TO THE PROJECT
============================================================

The folders in this zip match the real project paths. Every file goes to
the same path inside your IntelliJ project.

THIS COMPONENT NEEDS (must be on main first)
--------------------------------------------
 - FileHandler, User, AdminUser, login/session   (leader's User component)
 - Room, RoomService                             (Room component)
 - Reservation, ReservationService               (Reservation component)
 - common/navbar.jsp and css/style.css
Reservation is used only to decide whether a review is "Verified".
Merge Room and Reservation into main FIRST, press Ctrl+T, then start.

FILES AND WHERE THEY GO
-----------------------
model/    Review.java, PublicReview.java, VerifiedReview.java
          -> src/main/java/com/hotel/model/
service/  ReviewService.java
          -> src/main/java/com/hotel/service/
servlet/  ReviewListServlet (/reviews)
          AddReviewServlet (/addReview)
          EditReviewServlet (/editReview)
          DeleteReviewServlet (/deleteReview)
          -> src/main/java/com/hotel/servlet/
webapp/   reviewList.jsp, addReview.jsp, editReview.jsp
          -> src/main/webapp/feedback/
sample-data/reviews.txt -> sample-data/ (for the viva)

STEPS (commit after each)
-------------------------
 0. Ctrl+T to pull main. Git > New Branch, e.g.  feedback-yourname
 1. model:    3 classes.     Commit "Add Review classes"
 2. service:  ReviewService.  Commit "Add ReviewService"
 3. servlet:  4 servlets.    Commit "Add review servlets"
 4. pages:    3 JSPs.        Commit "Add review pages"
 5. Build > Build Project (no red errors). Stop Tomcat, Run.
 6. Open  .../reviews  and  .../addReview
 7. Push (Ctrl+Shift+K), then open a pull request into main.
How to add a file: right-click the folder > New > Java Class (or New > File
for .jsp), then Ctrl+A in the new file, paste, Ctrl+S.

TEST CHECKLIST
--------------
 - /reviews can be opened without logging in (read only, no buttons).
 - /addReview sends you to the login page if you are not logged in.
 - Write a review for a room you have booked: badge shows "Verified stay".
 - Write one for a room you have not booked: badge shows "Guest review".
 - A second review for the same room by the same user is rejected.
 - Rating outside 1-5 or an empty comment is rejected.
 - Edit changes rating and comment; the type and room stay the same.
 - Only the author or an admin sees Edit and Delete.
 - Pick a room in the filter: the average rating updates (verified = double).
 - Type a comment with commas and < > characters: it is saved and shown safely.
 - Open C:\Users\<you>\hotel-data\reviews.txt to see the data.

DATA FORMAT (reviews.txt)
-------------------------
id,userId,roomId,type,rating,date,comment
F001,U001,R101,VERIFIED,5,2026-11-14,Clean room&comma; friendly staff
Types: PUBLIC, VERIFIED.  Rating: 1 to 5.  Dates are yyyy-MM-dd.
Commas inside a comment are stored as &comma; so the line format never
breaks, and are turned back into commas when the file is read.

BUSINESS RULES
--------------
 - One review per user per room (edit it instead).
 - Type is decided by the system: VERIFIED if the user has a confirmed
   reservation for that room, otherwise PUBLIC.
 - Average rating is weighted: PUBLIC weight 1, VERIFIED weight 2.
   avg = sum(rating x weight) / sum(weight)
 - Comments are limited to 300 characters and HTML-escaped when displayed.

OOP MAP (for the viva)
----------------------
Encapsulation : private fields + getters/setters in Review
Abstraction   : Review is abstract; getType(), getWeight() and getBadge()
                are abstract
Inheritance   : PublicReview and VerifiedReview extends Review
Polymorphism  : getWeight()/getBadge() differ per subclass, so the same
                getWeightedRating() and the average give different results.
                ReviewService.createReview returns a Review that can be either.

FILE HANDLING MAP
-----------------
addReview  -> FileHandler.appendLine
getAllReviews -> readAllLines, each line -> object (lineToReview)
updateReview / deleteReview -> read all, change the list, writeAllLines

REQUEST FLOW
------------
addReview.jsp -> AddReviewServlet.doPost -> ReviewService.validate
-> decideType (checks ReservationService) -> addReview
-> FileHandler.appendLine -> reviews.txt -> redirect /reviews?roomId=...
-> ReviewListServlet -> reviewList.jsp

CRUD COVERAGE
-------------
Create: /addReview   Read: /reviews (+filter, search, average)
Update: /editReview  Delete: /deleteReview (author or admin)

NOTES
-----
 - Add a link in common/navbar.jsp (ask the leader):
   <a class="nav-link" href="<%= ctx %>/reviews">Reviews</a>
 - The Rooms list could link to  /reviews?roomId=R101  and
   /addReview?roomId=R101.
 - Understand every file before the viva. Ask your AI to explain each class
   line by line and to quiz you.
