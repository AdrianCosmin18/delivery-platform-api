package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.models.Product;

import java.util.List;

public interface RestaurantService {

    void addProduct(ProductDTO productDTO);
    void deleteProduct(String productName, String restaurantName);
    void addRestaurant(String restaurantName);
    void deleteRestaurant(String name);
    List<RestaurantDTO> getRestaurants();
    void putRestaurantInACity(String restaurantName, String cityName);
    void removeRestaurantFromCity(String restaurantName, String cityName);
}
