package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.*;
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

    void removeAddress(String email, long addressId);

    void updateAddress(String email, long addressId, AddressDTO addressDTO);

    List<AddressDTO> getUserAddresses(String email);

    void setAsMainAddress(String email, long addressId);

    boolean isAnyMainAddress(String email);

    AddressDTO getMainAddress(String email);

    void addCard(String email, CardDTO cardDTO);

    List<CardDTO> getUserCards(String email);

    void removeCard(String email, String cardNumber);

    void removeCard(String email, long cardId);

    void setAsMainCard(String email, long cardId);

    boolean isAnyMainCard(String email);

    CardDTO getMainCard(String email);

    @Transactional
    void placeOrder(CreateOrderRequest orderRequest);

    List<OrderDTO> getAllHistoryOrders(String email);

    void confirmReceivedOrder(String email, long orderId);

    void cancelOrder(String email, long orderId);

    UserDTO getUserByEmail(String email);

    User findByEmail(String email);

    Long findIdByUsername(String email);

    String findFirstNameByUsername(String email);

    void updateUser(String email, UserDTO userDTO);

    void makeUserAsAdmin(String email);

//    boolean areProductsFromOtherRestaurantInCart(String email, String restaurantName, String currentProductName);

//    void addProductToUserCart(String email, String restaurantName, String currentProductName, Integer quantity);
}
