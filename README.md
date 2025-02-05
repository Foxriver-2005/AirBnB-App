# Airbnb Booking App - Java (Android) # <br>
This Airbnb Booking App allows customers to book rooms at various Airbnbs and make payments securely using MPESA 
(a mobile money service). The owners can manage their listings, approve bookings, and view performance statistics. 
The app is developed using Java and designed specifically for Android. <br>
This open-source project is available for anyone to fork or contribute via pull requests (PRs).
## Project Overview ##
### Customers ###
1. **Login & Registration** <br>
    The customer logs into the app using their phone number. If a customer registers as a new user, they will input basic information and proceed with the registration.
2. **Browse Airbnbs** <br>
  The customer can browse various airbnbs based on different criteria such as:
  * Location
  * Ratings
  * Amenities
  * Images of the rooms and property sections
  * Price per room per day
3. **Booking Process** <br>
  The customer selects the room they wish to book and specifies:
    * The number of rooms
    * The number of days of stay
    * Total cost is automatically calculated based on the room(s) and duration.
    * Once the selection is made, the customer submits the booking request.
5. **Approval & Payment** <br>
  * The booking request goes to the airbnb owner for approval. The owner checks the room availability and approves the request.
  * The customer is notified via email about the approval and can proceed with payment.
  * The customer pays 10% of the total booking price during the booking process.
  * The remaining 90% of the total price is due upon arrival at the property (paid physically at the front desk).
  * Payment is processed via MPesa. A prompt is sent to the customer to enter their MPesa details (number and PIN) to complete the transaction.
  * Upon successful payment, the customer receives confirmation via email.
6. **Rate & Review** <br>
  Customers can rate and review the property only if they have completed a booking that was approved by the owner or if they have previously stayed there.

### Airbnb Owners ###
1. **Login & Registration**
* Airbnb owners (referred to as "Merchants") register with detailed information such as
  * Business location
  * Number of rooms
  * Property details (images, amenities, star rating, etc.)
* For security reasons, the owner logs in using their phone number, but the login must be done without the country code (+254), instead just the "4" for Kenyan phone numbers. Example: +254712345678 becomes 4712345678.
2. **Owner Dashboard** <br>
  ***Download Reports:*** Allows the owner to download a PDF report showing a detailed statement of the total orders received, customers’ names, amounts, and overall earnings. <br>
  ***Statistics:*** Displays real-time information such as:
      * Total orders received
      * Total earnings
      * Order average per month
      * Earnings average per month
  ***Visual Reports:*** Includes responsive charts to show the current month’s orders and earnings (line graph) as well as a bar chart showing the current year’s statistics.

3. **Airbnb Management** <br>
    The Bnb Overview Page provides detailed information about the property, including: <br>
    * The list of rooms and their availability (rooms can be scrolled for easy navigation). <br>
   **Sidebar Menu** <br>
        Provides access to various management options, such as: <br>
        * *Hotel Info:* Displays property details.
        * *Update Info:* Allows the owner to update general information about the property.
        * *Update Rooms:* Lets the owner edit, delete, or modify room information.
        * *Add Photos:* Allows the owner to upload new photos of various areas of the property (e.g., rooms, pool, conference areas).
        * *Add Rooms:* Enables owners to add new rooms with detailed descriptions and pricing.
        * *Need Approval:* Displays orders awaiting approval from customers, which the owner can accept after checking availability.
        * *Add Amenities:* Allows the owner to list amenities such as Wi-Fi, TV, conference rooms, etc.
        * *Orders Received:* Displays all confirmed bookings with customer details, payment status, and order history.
5. **Email Notifications for Owners** <br>
    * Owners are notified by email when a customer submits a booking request.
    * After approving the booking and receiving confirmation of the customer’s payment, the owner is sent a final email confirming the transaction.

## Key Features & Logic ##
### Customer Pages ###
1. **Dashboard**
    * *Search Bar:* Allows customers to search airbnbs based on names, locations, cities, countries.
    * *Featured Airbnb Section:* Shows the best or most booked airbnbs.
    * *Star Rating Filter:* Customers can filter airbnbs based on their desired star ratings (3, 4, or 5 stars).
    * *Map Search:* Displays available properties on a map for easier selection.
2. **Dropdown Menu on Dashboard**
    * *Home:* Takes the user back to the main dashboard.
    * *Search:* Directs the user to search for airbnbs.
    * *All Hotels:* Displays a list of all registered airbnbs.
    * *Favorites:* Shows a list of airbnbs marked as favorites by the user.
    * *Saved Hotels:* Displays the list of properties the customer has saved for later.
    * *Profile:* Opens the user’s profile page.
3. **Profile Page** <br>
    Displays user details (e.g., name, phone number, email, profile picture, bio, etc.).
    ***Sections***:
        * *Orders Given:* Shows a history of all orders made by the customer.
        * *Favorites:* Displays all the airbnbs the customer has marked as favorites.
        * *Watch Later:* Displays saved airbnbs the customer has marked for later.
        * *Upcoming Orders:* Shows all orders awaiting approval.
        * *Pay Now:* Displays approved orders where the customer can make the final payment via MPesa.

### Customer Pages ###
1. **Owner Dashboard**
    * Displays general statistics and earnings data.
    * Responsive line and bar charts to show orders and earnings trends.
2. **Bnb Overview**
    * Displays information about the rooms, availability, and amenities.
    * Ability to update and manage the property and rooms.
3. **Approval System** <br>
    Received orders are displayed under the "Need Approval" section for the owner to approve or reject based on room availability.
## Security & Assurances ##
1. **Payment Structure**
    * *10% payment at the time of booking:* Customers are required to pay 10% of the total cost when making a booking.
    * *Remaining 90% paid at the property:* The rest of the amount is paid upon arrival at the airbnb front desk.
2. **Rating & Reviews**
    * *Customer Eligibility:* Customers can only leave a rating or review after their booking has been approved by the owner and after their stay.
3. **Login Security** <br>
For owners, the phone number provided during registration must be used without the country code when logging in. For example, +254712345678 should be entered as 4712345678.

## Contribution ##
This project is open-source and we welcome contributions! Feel free to fork the repository, make changes, and submit a pull request.
#### How to Contribute ####
* Fork the repository.
* Clone your fork.
   * *git clone https://github.com/Foxriver-2005/AirBnB-App.git* 
* Create a new branch for your feature or bugfix
    * *git checkout -b feature/new-feature*
* Make your changes and commit
    * *git commit -am "Add new feature"*
* Push to your fork
    * *git push origin feature/new-feature*
* Open a pull request on GitHub.
