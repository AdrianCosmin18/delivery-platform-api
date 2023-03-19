package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.Card;
import com.example.deliveryapp.models.Restaurant;
import com.example.deliveryapp.models.User;
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



}
