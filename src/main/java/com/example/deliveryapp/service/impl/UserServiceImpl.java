package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.DTOs.CardDTO;
import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.*;
import com.example.deliveryapp.repos.CardRepo;
import com.example.deliveryapp.repos.CityRepo;
import com.example.deliveryapp.repos.RestaurantRepo;
import com.example.deliveryapp.repos.UserRepo;
import com.example.deliveryapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private CityRepo cityRepo;
    @Autowired
    private CardRepo cardRepo;

    private ModelMapper mapper;

    public UserServiceImpl(){
        this.mapper = new ModelMapper();
    }

    @Override
    public List<User> getUsers(){

        try{
            return this.userRepo.findAll();
        }catch (Exception e){
            throw new DeliveryCustomException("Something went wrong");
        }
    }

    @Override
    public void addUser(UserDTO userDTO){
        if(this.userRepo.getUserByEmail(userDTO.getEmail()).isPresent()){
            throw new DeliveryCustomException("Already exists a user with this email");
        }
        if(this.userRepo.getUserByPhone(userDTO.getPhone()).isPresent()){
            throw new DeliveryCustomException("This phone number belongs to someone else");
        }
        userRepo.save(this.mapper.map(userDTO, User.class));
    }

    @Override
    public void addRestaurantToWishlist(String email, String restaurantName){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        if(user.getRestaurants().stream().anyMatch(r -> r.getName().toLowerCase().equals(restaurantName.toLowerCase()))){
            throw new DeliveryCustomException("User already has this restaurant in his wishlist");
        }

        user.addRestaurant(restaurant);
        this.userRepo.save(user);
    }

    @Override
    public void removeRestaurantFromWishlist(String email, String restaurantName){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        if(user.getRestaurants().stream().anyMatch(r -> r.getName().toLowerCase().equals(restaurantName.toLowerCase()))){
            user.removeRestaurant(restaurant);
            userRepo.save(user);
        }else{
            throw new DeliveryCustomException("User doesn't have this restaurant in his wishlist");
        }
    }

    @Override
    public void addAddress(String email, AddressDTO addressDTO){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        List<Address> userAddresses = user.getAddresses();

        City city = this.cityRepo.getCityByName(addressDTO.getCityName())
                .orElseThrow(() -> new DeliveryCustomException("There is no city with this name in the db"));

        Address address = new Address(addressDTO.getStreet(), addressDTO.getNumber());
        if(addressDTO.getIsDefault()){

            userAddresses.forEach(adr -> adr.setIsDefault(false));
            address.setIsDefault(true);
        }else{

            address.setIsDefault(false);
        }
        address.setCity(city);
        address.setUser(user);

        if(userAddresses.stream().anyMatch(adr -> adr.compare(address))){
            throw new DeliveryCustomException("User already has this address");
        }

        user.addAddress(address);
        this.userRepo.save(user);
    }

    @Override
    public void removeAddress(String email, AddressDTO addressDTO){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        City city = this.cityRepo.getCityByName(addressDTO.getCityName())
                .orElseThrow(() -> new DeliveryCustomException("There is no city with this name in the db"));

        List<Address> userAddresses = user.getAddresses();

        Address address = Address.builder()
                .street(addressDTO.getStreet())
                .number(addressDTO.getNumber())
                .city(city)
                .build();

        if(userAddresses.stream().anyMatch(adr -> adr.compare(address))){
            user.deleteAddress(address);
            this.userRepo.save(user);
        }

        throw new DeliveryCustomException("User does not have this address");
    }

    @Override
    public List<Address> getUserAddresses(String email){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        return user.getAddresses();
    }

    @Override
    public void addCard(String email, CardDTO cardDTO) {

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        Card card = Card.builder()
                .cardNumber(cardDTO.getCardNumber())
                .cardHolderName(cardDTO.getCardHolderName())
                .securityCode(cardDTO.getSecurityCode())
                .expiryDate(cardDTO.getExpiryDate().atDay(1))
                .build();

        List<Card> cards = user.getCards();
        if (cards.stream().anyMatch(c -> c.equals(card))){
            throw new DeliveryCustomException("Already added this card");
        }

        card.setUser(user);
        user.addCard(card);
        this.userRepo.save(user);
    }

    @Override
    public List<CardDTO> getUserCards(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        if(user.getCards().isEmpty()){
            return new ArrayList<>();
        }

        List<CardDTO> cardsDto = new ArrayList<>();
        for(Card c: user.getCards()){

            CardDTO cardDTO = CardDTO.builder()
                    .cardHolderName(c.getCardHolderName())
                    .cardNumber(c.getCardNumber())
                    .securityCode(c.getSecurityCode())
                    .expiryDate(YearMonth.of(c.getExpiryDate().getYear(), c.getExpiryDate().getMonth()))
                    .build();

            cardsDto.add(cardDTO);
        }

        return cardsDto;

    }

    @Override
    public void removeCard(String email, String cardNumber){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        List<Card> userCards = user.getCards();

        if(userCards.stream().anyMatch(card -> card.getCardNumber().equals(cardNumber))){

            List<Card> cards = userCards.stream().filter(card1 -> card1.getCardNumber().equals(cardNumber)).collect(Collectors.toList());
            user.deleteCard(cards.get(0));

            this.userRepo.save(user);
        }else{
            throw new DeliveryCustomException("User does not own this card");
        }
    }
}
