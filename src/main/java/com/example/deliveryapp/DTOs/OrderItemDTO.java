package com.example.deliveryapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private Double price;
    private Integer quantity;
    private String extraIngredients;// "sos Big Mac,salata verde,rosii" - lista cu ingrediente extra
    private String lessIngredients;// "ceapa, castraveti murati" - lista cu ingredientele pe care le scoatem din burger
    private Double extraIngredientsPrice;
    private String productName;
    private String restaurantName;
}
