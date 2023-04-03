package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.DTOs.CardDTO;
import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.models.Address;
import com.example.deliveryapp.models.User;
import com.example.deliveryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("delivery-app/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public void addUser(@Valid @RequestBody UserDTO userDTO){
        this.userService.addUser(userDTO);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<List<User>>(this.userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/add-restaurant-to-wishlist")
    public void addRestaurantToWishlist(@RequestParam(value = "email")String email, @RequestParam(value = "restaurantName")String restaurantName){
        this.userService.addRestaurantToWishlist(email, restaurantName);
    }

    @DeleteMapping("/delete-restaurant-from-wishlist")
    public void removeRestaurantFromWishlist(@RequestParam(value = "email")String email, @RequestParam(value = "restaurantName")String restaurantName){
        this.userService.removeRestaurantFromWishlist(email, restaurantName);
    }

    @PostMapping("/add-address")
    public void addAddress(@RequestParam(value = "email")String email, @RequestBody AddressDTO addressDTO){
        this.userService.addAddress(email, addressDTO);
    }

    @GetMapping("/get-user-addresses/{email}")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable String email){
        return new ResponseEntity<List<Address>>(this.userService.getUserAddresses(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-address/{email}")
    public void deleteAddress(@PathVariable String email, @RequestBody AddressDTO addressDTO){
        this.userService.removeAddress(email, addressDTO);
    }

    @PostMapping("/add-card/{email}")
    public void addCard(@PathVariable String email, @RequestBody CardDTO cardDTO){
        this.userService.addCard(email, cardDTO);
    }

    @GetMapping("/get-user-cards/{email}")
    public ResponseEntity<List<CardDTO>> getUserCards(@PathVariable String email){
        return new ResponseEntity<>(this.userService.getUserCards(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-card")
    public void deleteCard(@RequestParam(value = "email")String email, @RequestParam(value = "cardNumber")String cardNumber){
        this.userService.removeCard(email, cardNumber);
    }

    @PostMapping("/add-product-to-cart/{email}")
    public void addProductToCart(
            @PathVariable String email,
            @RequestParam(value = "restaurantName") String restaurantName,
            @RequestParam(value = "productName") String productName,
            @RequestParam(value = "quantity") Integer quantity){

        this.userService.addProductToUserCart(email, restaurantName, productName, quantity);
    }
}
