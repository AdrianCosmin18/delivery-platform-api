package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.*;
import com.example.deliveryapp.repos.CityRepo;
import com.example.deliveryapp.repos.RestaurantRepo;
import com.example.deliveryapp.repos.UserRepo;
import com.example.deliveryapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private CityRepo cityRepo;

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

        Address address = Address.builder()
                .street(addressDTO.getStreet())
                .number(addressDTO.getNumber())
                .city(city)
                .build();

        user.deleteAddress(address);
        this.userRepo.save(user);
    }

    @Override
    public List<Address> getUserAddresses(String email){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        return user.getAddresses();
    }



}
