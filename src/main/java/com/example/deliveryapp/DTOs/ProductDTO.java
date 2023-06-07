package com.example.deliveryapp.DTOs;

import com.example.deliveryapp.models.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

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
    private Boolean containsLactose;
    private Boolean containsGluten;
    private Boolean isVegetarian;


    public ProductDTO(String name, Double price, String type, String description, String ingredients, String restaurantName) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
        this.ingredients = ingredients;
        this.restaurantName = restaurantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDTO)) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getType(), that.getType()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getIngredients(), that.getIngredients()) && Arrays.equals(getImage(), that.getImage()) && Objects.equals(getRestaurantName(), that.getRestaurantName());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getName(), getPrice(), getType(), getDescription(), getIngredients(), getRestaurantName());
        result = 31 * result + Arrays.hashCode(getImage());
        return result;
    }
}
