# Delivery App - "Burger Shop"

1) Demo

  https://drive.google.com/file/d/1VVFwoVGNYpTrD29senLdwrWYw1tpLMg_/view?usp=sharing

2)  Abstract

  The "BurgerShop" application aims to provide users with a simple and efficient platform to take advantage of the products found on the restaurant menu from the comfort of their home. Within this application, the specific functionalities of a food delivery application have been implemented, where users have access to different products such as burgers, drinks, fries and sauces. They can add the products shown in the menu to their order, as well as edit them according to their preferences. When placing an order, customers can manage the delivery address and payment options.
  
  The interface of the application allows users to track the status of the order until it is successfully delivered, so the application stands out for its complexity, where the user is connected with the interface throughout the delivery process.

3) Technologies used to develop the app

   Backend: Java
     -  Spring Boot
     -  Spring Security
     -  Hibernate
     -  JWT Token
  
   Frontend: Angular
     -  HTML
     -  CSS
     -  Typescript
     -  NPM
     -  Redux
     -  NgRx LocalStorage
   
   Databse: MySQL

4)  Diagram db


![image](https://github.com/AdrianCosmin18/delivery-platform-api/assets/91340261/eb3dc1eb-f2dc-4cb3-a395-68bbdbedea40)


**Bussiness Requirements:**

1) Authentication and registration

Users who do not have an account should be able to navigate through the application without being registered, being able to view information about the menu. However, they should not be able to place an order. Their status may change and they may only be able to place the order if they register.

Users who have an account have the opportunity to log in to the application to benefit from the functionality offered. It would access all sections including the admin section, "My Cart" or "My Preferences".

2) Product and menu management

The application should allow adding, updating and deleting products from the database. Users must be able to view and navigate menus with clear options and information about each product. Information about menus should be found on the main page, options and product details should be accessible after the user selects a particular product. The options that the user could benefit from are focused on adding products to the cart, editing them, in terms of adding or removing different ingredients, but also placing an order.

3) Order system

Users should be able to place orders for the products they want. Administration must also have access to a list of orders to manage, confirm and update their status.

Admins should also build a chart that highlights their best and worst selling products for better customer relationship.

4) Tracking delivery

Users must be able to track the delivery status of their orders in real time. This may include notifications about order status and driver location.

This feature should be accessed from the "My commands" section, for a more intuitive approach between the interaction between the user and the application.

5) Online payment system

The app should provide safe and easy-to-use options for online payments. This may involve integrating with various payment methods and ensuring transaction security.

For this, the "My Cards" section should be created, which would facilitate user interaction with the interface.

6) Data validations

For any form that requires data entry, the system should offer warnings, in the form of notifications, indicating different types of errors, such as non-compliance with the maximum number of characters entered in a field.

7) Account management

Users should be able to manage their accounts, including personal information, shipping addresses and order history. All these sections should be accessible from the toolbar.

8) Interaction system with the supplier

the user must have a section intended for interaction with the supplier, for communication of particular information regarding the order or the method of delivery or to be able to offer him remuneration in advance.

9) Product filtering

The system should facilitate the user's interaction with the application, providing the "My preferences" and "Food intolerance" sections, allowing the user to customize the products according to preferences.

10) Performance and scalability

Ensuring high application performance and scalability to handle growing user and data volumes.

**Features:**

1) User account management

It allows users to create and manage their accounts. Here, the user is shown, in the form of a form with several inputs, his personal data already filled in from the database. He can edit any field of his data, by changing the input and pressing the "Save" button.

User account management also includes secure authentication and password recovery. The application has a password change functionality in case the user believes that it no longer meets their standards or may be compromised.

2) Placement of orders

Users have the opportunity to choose the products they want and add them to the cart, after which they can place an order. They also benefit from a functionality for managing the shopping cart. The order placement page consists of the section containing the address selected for delivery and the payment card.

3) Track deliveries

Users can track the status of orders and deliveries in real time. If the user wants to see information about the order or its status, he can access the "Order history" section from the customer menu.

4) Admin account management

The application administrator has the possibility to benefit from all the permissions that the user has, so his interface will be similar to that of the user. What differentiates a user from an admin at the interface level is a page that can be found in the main admin menu under the “Log out” section.

The main role of the administrator in the application is the handling of orders, so he will have information about all orders made by all customers in each city.

The administrator is the one who performs the permutation of an order from one stage to another when he considers it necessary. The stages of an order are: order placed, payment confirmed, order in preparation, order being delivered, order delivered and order cancelled.

5) Order history

This section displays all the order history of an authenticated user. Each order is described in the form of a card that contains details about the status of the order, its id, the date and time it was placed, the delivery address, the card number the customer chose to pay with and the total amount of money allocated order.


