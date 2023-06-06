package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.*;
import com.example.deliveryapp.models.Address;
import com.example.deliveryapp.models.OrderItem;
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
    @PreAuthorize("hasAnyRole('ROLE_USER')")
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
    public ResponseEntity<List<AddressDTO>> getUserAddresses(@PathVariable String email){
        return new ResponseEntity<List<AddressDTO>>(this.userService.getUserAddresses(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-address/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void deleteAddress(@PathVariable String email, @RequestBody AddressDTO addressDTO){
        this.userService.removeAddress(email, addressDTO);
    }

    @DeleteMapping("/delete-address/{email}/{addressId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void deleteAddress(@PathVariable String email, @PathVariable long addressId){
        this.userService.removeAddress(email, addressId);
    }

    @PutMapping("/set-as-main-address/{email}/{addressId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void setAsMainAddress(@PathVariable String email, @PathVariable long addressId){
        this.userService.setAsMainAddress(email, addressId);
    }

    @PutMapping("/update-address/{email}/{addressId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void updateAddress(@PathVariable String email, @PathVariable long addressId, @RequestBody AddressDTO addressDTO){
        this.userService.updateAddress(email, addressId, addressDTO);
    }

    @GetMapping("/get-main-address/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<AddressDTO> getMainAddressOfUser(@PathVariable String email){
        return new ResponseEntity<>(this.userService.getMainAddress(email), HttpStatus.OK);
    }

    @GetMapping("/has-user-main-address/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public boolean hasUserMainAddress(@PathVariable String email){
        return this.userService.isAnyMainAddress(email);
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

    @DeleteMapping("/delete-card/{email}/{cardId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void deleteCard(@PathVariable(value = "email")String email, @PathVariable(value = "cardId")long cardId){
        this.userService.removeCard(email, cardId);
    }

    @PutMapping("/set-as-main-card/{email}/{cardId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void setAsMainCard(@PathVariable String email, @PathVariable long cardId){
        this.userService.setAsMainCard(email, cardId);
    }

    @GetMapping("/has-user-main-card/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public boolean setAsMainCard(@PathVariable String email){
        return this.userService.isAnyMainCard(email);
    }

    @GetMapping("/get-user-main-card/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CardDTO getUserMainCard(@PathVariable String email){
        return this.userService.getMainCard(email);
    }

    @PostMapping("/place-order")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void placeOrder(@RequestBody CreateOrderRequest orderRequest){
        this.userService.placeOrder(orderRequest);
    }

    @GetMapping("/history-orders/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<OrderDTO> getHistoryOfOrders(@PathVariable String email){
        return this.userService.getAllHistoryOrders(email);
    }

    @PutMapping("/update-user/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void updateUser(@PathVariable String email, @RequestBody UserDTO userDTO){
        this.userService.updateUser(email, userDTO);
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
