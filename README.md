# Airbnb Booking App - Java (Android) #
This Airbnb Booking App allows customers to book rooms at various Airbnbs and make payments securely using MPESA 
(a mobile money service). The owners can manage their listings, approve bookings, and view performance statistics. 
The app is developed using Java and designed specifically for Android. <br>
This open-source project is available for anyone to fork or contribute via pull requests (PRs).
## Project Overview ##
### Customers ###
1. **Login & Registration**
    The customer logs into the app using their phone number. If a customer registers as a new user, they will input basic information and proceed with the registration.
2. **Browse Airbnbs**
  The customer can browse various airbnbs based on different criteria such as: <br>
  * Location
  * Ratings
  * Amenities
  * Images of the rooms and property sections
  * Price per room per day
3. **Booking Process**
  The customer selects the room they wish to book and specifies: <br>
    * The number of rooms
    * The number of days of stay
    * Total cost is automatically calculated based on the room(s) and duration.
    * Once the selection is made, the customer submits the booking request.
5. **Approval & Payment**
  * The booking request goes to the airbnb owner for approval. The owner checks the room availability and approves the request.
  * The customer is notified via email about the approval and can proceed with payment.
  * The customer pays 10% of the total booking price during the booking process.
  * The remaining 90% of the total price is due upon arrival at the property (paid physically at the front desk).
  * Payment is processed via MPesa. A prompt is sent to the customer to enter their MPesa details (number and PIN) to complete the transaction.
  * Upon successful payment, the customer receives confirmation via email.
6. **Rate & Review**
  Customers can rate and review the property only if they have completed a booking that was approved by the owner or if they have previously stayed there.

### Airbnb Owners ###
1. **Login & Registration**
* Airbnb owners (referred to as "Merchants") register with detailed information such as
  * Business location
  * Number of rooms
  * Property details (images, amenities, star rating, etc.)
* For security reasons, the owner logs in using their phone number, but the login must be done without the country code (+254), instead just the "4" for Kenyan phone numbers. Example: +254712345678 becomes 4712345678.
2. **Owner Dashboard**
  ***Download Reports:*** Allows the owner to download a PDF report showing a detailed statement of the total orders received, customers’ names, amounts, and overall earnings.
  ***Statistics:** Displays real-time information such as:
      * Total orders received
      * Total earnings
      * Order average per month
      * Earnings average per month
  ***Visual Reports:*** Includes responsive charts to show the current month’s orders and earnings (line graph) as well as a bar chart showing the current year’s statistics.

3. **Airbnb Management**
    The Bnb Overview Page provides detailed information about the property, including:
    * The list of rooms and their availability (rooms can be scrolled for easy navigation).
   **Sidebar Menu** provides access to various management options, such as:
        * *Hotel Info:* Displays property details.
        * *Update Info:* Allows the owner to update general information about the property.
        * *Update Rooms:* Lets the owner edit, delete, or modify room information.
        * *Add Photos:* Allows the owner to upload new photos of various areas of the property (e.g., rooms, pool, conference areas).
        * *Add Rooms:* Enables owners to add new rooms with detailed descriptions and pricing.
        * *Need Approval:* Displays orders awaiting approval from customers, which the owner can accept after checking availability.
        * *Add Amenities:* Allows the owner to list amenities such as Wi-Fi, TV, conference rooms, etc.
        * *Orders Received:* Displays all confirmed bookings with customer details, payment status, and order history.
5. **Email Notifications for Owners**
    * Owners are notified by email when a customer submits a booking request.
    * After approving the booking and receiving confirmation of the customer’s payment, the owner is sent a final email confirming the transaction.


