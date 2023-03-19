package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("delivery-app/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/{restaurantName}")
    public void addRestaurant(@PathVariable String restaurantName){
        this.restaurantService.addRestaurant(restaurantName);
    }

    @GetMapping()
    public ResponseEntity<List<RestaurantDTO>> getRestaurants(){
        return new ResponseEntity<List<RestaurantDTO>>(this.restaurantService.getRestaurants(), HttpStatus.OK);
    }

    @PostMapping("/put-restaurant-to-city")
    public void putRestaurantInACity(@RequestParam(value = "restaurantName")String restaurantName, @RequestParam(value = "cityName") String cityName){
        this.restaurantService.putRestaurantInACity(restaurantName, cityName);
    }

    @DeleteMapping("/{restaurantName}")
    public void deleteRestaurant(@PathVariable String restaurantName){
        this.restaurantService.deleteRestaurant(restaurantName);
    }

    @DeleteMapping("/remove-restaurant-from-city")
    public void removeRestaurantFromCity(@RequestParam(value = "restaurantName")String restaurantName, @RequestParam(value = "cityName") String cityName){
        this.restaurantService.removeRestaurantFromCity(restaurantName, cityName);
    }



}
