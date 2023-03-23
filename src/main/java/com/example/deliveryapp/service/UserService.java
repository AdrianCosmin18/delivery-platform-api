package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.models.Address;
import com.example.deliveryapp.models.Card;
import com.example.deliveryapp.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    List<User> getUsers();
    void addUser(UserDTO userDTO);
    void addRestaurantToWishlist(String email, String restaurantName);
    void removeRestaurantFromWishlist(String email, String restaurantName);

    void addAddress(String email, AddressDTO addressDTO);

    void removeAddress(String email, AddressDTO addressDTO);

    List<Address> getUserAddresses(String email);
}
