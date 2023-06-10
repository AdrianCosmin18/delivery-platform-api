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

    private Long id;
    private String name;
    private Double price;
    private String type;
    private String description;
    private String ingredients;
    private long imageId;
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
        return getImageId() == that.getImageId() && Objects.equals(getName(), that.getName()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getType(), that.getType()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getIngredients(), that.getIngredients()) && Objects.equals(getRestaurantName(), that.getRestaurantName()) && Objects.equals(getContainsLactose(), that.getContainsLactose()) && Objects.equals(getContainsGluten(), that.getContainsGluten()) && Objects.equals(getIsVegetarian(), that.getIsVegetarian());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getType(), getDescription(), getIngredients(), getImageId(), getRestaurantName(), getContainsLactose(), getContainsGluten(), getIsVegetarian());
    }
}
