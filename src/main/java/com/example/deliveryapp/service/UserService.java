package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.DTOs.CardDTO;
import com.example.deliveryapp.DTOs.CreateOrderRequest;
import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.models.Address;
import com.example.deliveryapp.models.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<User> getUsers();
    void addUser(UserDTO userDTO);
    void addRestaurantToWishlist(String email, String restaurantName);
    void removeRestaurantFromWishlist(String email, String restaurantName);

    void addAddress(String email, AddressDTO addressDTO);

    void removeAddress(String email, AddressDTO addressDTO);

    List<Address> getUserAddresses(String email);

    void addCard(String email, CardDTO cardDTO);

    List<CardDTO> getUserCards(String email);

    void removeCard(String email, String cardNumber);

    @Transactional
    void placeOrder(CreateOrderRequest orderRequest);

    UserDTO getUserByEmail(String email);

//    boolean areProductsFromOtherRestaurantInCart(String email, String restaurantName, String currentProductName);

//    void addProductToUserCart(String email, String restaurantName, String currentProductName, Integer quantity);
}
