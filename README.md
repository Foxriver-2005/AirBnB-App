# Airbnb Booking App - Java (Android) #
This Airbnb Booking App allows customers to book rooms at various Airbnbs and make payments securely using MPESA 
(a mobile money service). The owners can manage their listings, approve bookings, and view performance statistics. 
The app is developed using Java and designed specifically for Android.
This open-source project is available for anyone to fork or contribute via pull requests (PRs).
## Project Overview ##
### Customers ###
1. % Login & Registration
    The customer logs into the app using their phone number. If a customer registers as a new user, they will input basic information and proceed with the registration.
2. % Browse Airbnbs
  The customer can browse various airbnbs based on different criteria such as:
  Location
  Ratings
  Amenities
  Images of the rooms and property sections
  Price per room per day
3. % Booking Process
  The customer selects the room they wish to book and specifies:
    i.The number of rooms
    ii. The number of days of stay
    iii. Total cost is automatically calculated based on the room(s) and duration.
  Once the selection is made, the customer submits the booking request.
5. % Approval & Payment
  The booking request goes to the airbnb owner for approval. The owner checks the room availability and approves the request.
  The customer is notified via email about the approval and can proceed with payment.
  The customer pays 10% of the total booking price during the booking process.
  The remaining 90% of the total price is due upon arrival at the property (paid physically at the front desk).
  Payment is processed via MPesa. A prompt is sent to the customer to enter their MPesa details (number and PIN) to complete the transaction.
  Upon successful payment, the customer receives confirmation via email.
6. % Rate & Review
  Customers can rate and review the property only if they have completed a booking that was approved by the owner or if they have previously stayed there.

### Airbnb Owners ###

