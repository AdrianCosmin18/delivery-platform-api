package com.example.deliveryapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String name;
    private Double price;
    private String type;
    private String description;
    private String ingredients;
    private byte[] picture;
    private String restaurantName;

    public ProductDTO(String name, Double price, String type, String description, String ingredients, String restaurantName) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
        this.ingredients = ingredients;
        this.restaurantName = restaurantName;
    }
}
