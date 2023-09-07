package com.example.deliveryapp.user;

import com.example.deliveryapp.DTOs.*;
import com.example.deliveryapp.email.EmailSenderService;
import com.example.deliveryapp.user.User;
import com.example.deliveryapp.security.jwt.JWTTokenProvider;
import com.example.deliveryapp.user.UserService;
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
    @Autowired
    private EmailSenderService emailSenderService;

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
        this.emailSenderService.sendEmail(
                newUser.getEmail(),
                "Bun venit în aplicația BurgerShop!",
                this.formEmailMessage(userDTO.getFirstName(), userDTO.getLastName()));
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

    @PutMapping("/change-password/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void changePassword(@PathVariable String email, @RequestParam(value = "oldPassword") String oldPassword, @RequestParam(value = "newPassword") String newPassword){
        this.authenticate(email, oldPassword);
        this.userService.changePassword(email, newPassword);

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
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email){
        return new ResponseEntity<>(this.userService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/get-user")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(){
        return new ResponseEntity<>(this.userService.getUser(), HttpStatus.OK);
    }

    @PostMapping("/add-restaurant-to-wishlist")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void addRestaurantToWishlist(@RequestParam(value = "email")String email, @RequestParam(value = "restaurantName")String restaurantName){
        this.userService.addRestaurantToWishlist(email, restaurantName);
    }

    @DeleteMapping("/delete-restaurant-from-wishlist")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void removeRestaurantFromWishlist(@RequestParam(value = "email")String email, @RequestParam(value = "restaurantName")String restaurantName){
        this.userService.removeRestaurantFromWishlist(email, restaurantName);
    }

    @PostMapping("/add-address")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void addAddress(@RequestBody AddressDTO addressDTO){
        this.userService.addAddress(addressDTO);
    }

    @GetMapping("/get-user-addresses/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(@PathVariable String email){
        return new ResponseEntity<List<AddressDTO>>(this.userService.getUserAddresses(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-address/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteAddress(@PathVariable String email, @RequestBody AddressDTO addressDTO){
        this.userService.removeAddress(email, addressDTO);
    }

    @DeleteMapping("/delete-address/{addressId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteAddress(@PathVariable long addressId){
        this.userService.removeAddress(addressId);
    }

    @PutMapping("/set-as-main-address/{email}/{addressId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void setAsMainAddress(@PathVariable String email, @PathVariable long addressId){
        this.userService.setAsMainAddress(email, addressId);
    }

    @PutMapping("/update-address/{email}/{addressId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void updateAddress(@PathVariable String email, @PathVariable long addressId, @RequestBody AddressDTO addressDTO){
        this.userService.updateAddress(email, addressId, addressDTO);
    }

    @GetMapping("/get-main-address/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AddressDTO> getMainAddressOfUser(@PathVariable String email){
        return new ResponseEntity<>(this.userService.getMainAddress(email), HttpStatus.OK);
    }

    @GetMapping("/has-user-main-address/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public boolean hasUserMainAddress(@PathVariable String email){
        return this.userService.isAnyMainAddress(email);
    }

    @PostMapping("/add-card/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void addCard(@PathVariable String email, @RequestBody CardDTO cardDTO){
        this.userService.addCard(email, cardDTO);
    }

    @GetMapping("/get-user-cards/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<CardDTO>> getUserCards(@PathVariable String email){
        return new ResponseEntity<>(this.userService.getUserCards(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-card/{email}/{cardId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteCard(@PathVariable(value = "email")String email, @PathVariable(value = "cardId")long cardId){
        this.userService.removeCard(email, cardId);
    }

    @PutMapping("/set-as-main-card/{email}/{cardId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void setAsMainCard(@PathVariable String email, @PathVariable long cardId){
        this.userService.setAsMainCard(email, cardId);
    }

    @GetMapping("/has-user-main-card/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public boolean setAsMainCard(@PathVariable String email){
        return this.userService.isAnyMainCard(email);
    }

    @GetMapping("/get-user-main-card/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public CardDTO getUserMainCard(@PathVariable String email){
        return this.userService.getMainCard(email);
    }

    @PostMapping("/place-order")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void placeOrder(@RequestBody CreateOrderRequest orderRequest){
        this.userService.placeOrder(orderRequest);
    }

    @GetMapping("/history-orders/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<OrderDTO> getHistoryOfOrders(@PathVariable String email){
        return this.userService.getAllHistoryOrders(email);
    }

    @PutMapping("/update-user/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void updateUser(@PathVariable String email, @RequestBody UserDTO userDTO){
        this.userService.updateUser(email, userDTO);
    }

    @PutMapping("/confirm-received-order/{email}/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void confirmReceivedOrder(@PathVariable String email, @PathVariable long orderId){
        this.userService.confirmReceivedOrder(email, orderId);
    }

    @DeleteMapping("/cancel-order/{email}/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void cancelOrder(@PathVariable String email, @PathVariable long orderId){
        this.userService.cancelOrder(email, orderId);
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

    @PutMapping("/make-user-as-admin/{email}")
    public void makeUserAsAdmin(@PathVariable String email){
        this.userService.makeUserAsAdmin(email);
    }

    public String formEmailMessage(String firstName, String lastName){

        return String.format("" +
                "Dragă %s %s, \n\n" +
                "Bine ai venit în aplicația noastră de livrare cu tematică de burgeri! Suntem încântați să te avem alături și să-ți oferim cele mai delicioase burgeri direct la ușa ta.\n\n" +
                "În aplicația noastră, vei putea:\n" +
                "\n" +
                "Explora meniul nostru variat de burgeri și produse complementare\n" +
                "Personaliza și comanda burgerul preferat, alegând ingredientele și toppingurile dorite\n" +
                "Vizualiza starea comenzii tale\n" +
                "Beneficia de oferte și reduceri exclusive pentru utilizatorii aplicației\n" +
                "Salvat și accesa comenzile și preferințele tale pentru o experiență rapidă și ușoară\n" +
                "\n" +
                "Suntem mândri să îți oferim cele mai proaspete ingrediente și combinații savuroase pentru a-ți satisface pofta de burgeri autentici și gustoși.\n" +
                "\n" +
                "Echipa noastră de bucătari talentați pregătește burgeri de cea mai înaltă calitate, folosind rețete tradiționale și ingrediente de încredere. Ne asigurăm că fiecare comandă este pregătită cu atenție și livrată în cel mai scurt timp posibil.\n" +
                "\n" +
                "Îți mulțumim pentru alegerea făcută și pentru încrederea acordată. Echipa noastră este mereu aici pentru a răspunde întrebărilor tale și a te ajuta în orice fel posibil. Nu ezita să ne contactezi prin intermediul funcției noastre de asistență online sau telefonic.\n" +
                "\n" +
                "Îți mulțumim pentru alegerea făcută și pentru încrederea acordată. Echipa noastră este mereu aici pentru a răspunde întrebărilor tale și a te ajuta în orice fel posibil. Nu ezita să ne contactezi prin intermediul funcției noastre de asistență online sau telefonic.\n" +
                "\n" +
                "Cu poftă,\n" +
                "Echipa BurgerShop", firstName, lastName);
    }
}
