package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.*;
import com.example.deliveryapp.models.Address;
import com.example.deliveryapp.models.User;
import com.example.deliveryapp.security.jwt.JWTTokenProvider;
import com.example.deliveryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.example.deliveryapp.security.utils.Util.JWT_TOKEN_HEADER;

@RestController
@RequestMapping("delivery-app/user")
@CrossOrigin()
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDTO userDTO){

        this.userService.addUser(userDTO);
        User newUser = this.userService.findByEmail(userDTO.getEmail());
        Collection<? extends GrantedAuthority> authorities = newUser.getAuthorities();

        HttpHeaders jwtHeader = getJwtHeader(newUser);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                newUser.getId(),
                newUser.getEmail(),
                newUser.getFirstName(),
                jwtHeader.getFirst(JWT_TOKEN_HEADER),
                authorities
        );

        return new ResponseEntity<>(authenticationResponse, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserDTO userDTO){
        this.authenticate(userDTO.getEmail(), userDTO.getPassword());
        User loggedUser = this.userService.findByEmail(userDTO.getEmail());
        Collection<? extends GrantedAuthority> authorities = loggedUser.getAuthorities();

        Long userId = this.userService.findIdByUsername(userDTO.getEmail());
        String firstName = this.userService.findFirstNameByUsername(userDTO.getEmail());
        HttpHeaders jwtHeader = getJwtHeader(loggedUser);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                userId,
                userDTO.getEmail(),
                firstName,
                jwtHeader.getFirst(JWT_TOKEN_HEADER),
                authorities
        );

        return new ResponseEntity<>(authenticationResponse, jwtHeader, HttpStatus.OK);
    }


    @PostMapping
    public void addUser(@Valid @RequestBody UserDTO userDTO){
        this.userService.addUser(userDTO);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<List<User>>(this.userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/get-user/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email){
        return new ResponseEntity<>(this.userService.getUserByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/add-restaurant-to-wishlist")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void addRestaurantToWishlist(@RequestParam(value = "email")String email, @RequestParam(value = "restaurantName")String restaurantName){
        this.userService.addRestaurantToWishlist(email, restaurantName);
    }

    @DeleteMapping("/delete-restaurant-from-wishlist")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void removeRestaurantFromWishlist(@RequestParam(value = "email")String email, @RequestParam(value = "restaurantName")String restaurantName){
        this.userService.removeRestaurantFromWishlist(email, restaurantName);
    }

    @PostMapping("/add-address")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void addAddress(@RequestParam(value = "email")String email, @RequestBody AddressDTO addressDTO){
        this.userService.addAddress(email, addressDTO);
    }

    @GetMapping("/get-user-addresses/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable String email){
        return new ResponseEntity<List<Address>>(this.userService.getUserAddresses(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-address/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void deleteAddress(@PathVariable String email, @RequestBody AddressDTO addressDTO){
        this.userService.removeAddress(email, addressDTO);
    }

    @PostMapping("/add-card/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void addCard(@PathVariable String email, @RequestBody CardDTO cardDTO){
        this.userService.addCard(email, cardDTO);
    }

    @GetMapping("/get-user-cards/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<List<CardDTO>> getUserCards(@PathVariable String email){
        return new ResponseEntity<>(this.userService.getUserCards(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-card")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void deleteCard(@RequestParam(value = "email")String email, @RequestParam(value = "cardNumber")String cardNumber){
        this.userService.removeCard(email, cardNumber);
    }

    @PostMapping("/place-order")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void placeOrder(@RequestBody CreateOrderRequest orderRequest){

        this.userService.placeOrder(orderRequest);
    }



    private HttpHeaders getJwtHeader(User user) {
        HttpHeaders headers = new HttpHeaders();

        // punem in header nou token generat cu numele: Jwt-Token
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
