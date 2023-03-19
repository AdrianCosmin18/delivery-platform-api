package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.models.Card;
import com.example.deliveryapp.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    List<User> getUsers();
    void addUser(UserDTO userDTO);
}
