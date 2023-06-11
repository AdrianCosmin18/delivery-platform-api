package com.example.deliveryapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateOrderRequest implements Serializable {

    private String emailUser;
    @Builder.Default
    private List<OrderItemDTO> productsInCart = new ArrayList<>();
    private Long cardId;
    private Long addressId;
    private Double productsAmount;
    private Double deliveryTax;
    private Double tipsTax;
    private Double totalAmount;
    private String commentsSection;

}
