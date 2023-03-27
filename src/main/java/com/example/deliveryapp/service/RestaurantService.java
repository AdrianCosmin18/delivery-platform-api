package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RestaurantService {

    void addProduct(MultipartFile file, ProductDTO productDTO) throws IOException;

    List<ProductDTO> getRestaurantProducts(String restaurantName);

    void deleteProduct(String productName, String restaurantName);
    void addRestaurant(String restaurantName);
    void deleteRestaurant(String name);
    List<RestaurantDTO> getRestaurants();
    void putRestaurantInACity(String restaurantName, String cityName);
    void removeRestaurantFromCity(String restaurantName, String cityName);
}
