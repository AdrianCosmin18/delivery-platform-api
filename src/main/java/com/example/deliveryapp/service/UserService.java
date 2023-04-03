package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.DTOs.CardDTO;
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

    void addCard(String email, CardDTO cardDTO);

    List<CardDTO> getUserCards(String email);

    void removeCard(String email, String cardNumber);

    boolean areProductsFromOtherRestaurantInCart(String email, String restaurantName, String currentProductName);

    //daca cosul e gol, adaugam in el produsul
    //daca nu e gol, verificam daca este un produs de la un alt restaurant
    //  daca este, atunci golim cosul si adaugam acest produs produs
    //daca nu e gol si este un produs de la acelasi restaurant, verificam daca este cumva acelasi produs
    //  cu cel pe care incercam sa il adaugam acum, daca sunt identice, doar crestem cantitea cu cat am mai adaugat
    //  daca nu este acelasi produs, atunci doar il adaugam
    void addProductToUserCart(String email, String restaurantName, String currentProductName, Integer quantity);
}
