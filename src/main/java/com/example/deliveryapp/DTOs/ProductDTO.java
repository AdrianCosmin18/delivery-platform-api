package com.example.deliveryapp.DTOs;

import com.example.deliveryapp.models.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

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
    private byte[] image;
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
