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
    private List<ProductCart> productsInCart = new ArrayList<>();
    private String cardNumber;
    private AddressDTO addressDTO;


}
