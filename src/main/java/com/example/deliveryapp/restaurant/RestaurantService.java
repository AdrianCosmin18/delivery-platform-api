package com.example.deliveryapp.restaurant;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface RestaurantService {


    void addProduct(
            MultipartFile file,
            String name,
            Double price,
            String type,
            String description,
            String ingredients,
            String restaurantName,
            boolean containsGluten,
            boolean containsLactose,
            boolean isVegetarian
    ) throws IOException;
    List<ProductDTO> getRestaurantProducts(String restaurantName, String type);
    void deleteProduct(String productName, String restaurantName);
    void addRestaurant(String restaurantName);
    void deleteRestaurant(String name);
    List<RestaurantDTO> getRestaurants();
    void putRestaurantInACity(String restaurantName, String cityName);
    void removeRestaurantFromCity(String restaurantName, String cityName);

    ProductDTO getProductByName(String restaurantName, String productName);

    Set<ProductDTO> getProductsByIngredients(String foodType, String ingredientList);
}
