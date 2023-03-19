package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.UserDTO;
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
}
