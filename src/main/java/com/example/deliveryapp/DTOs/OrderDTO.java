package com.example.deliveryapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private Double amount;
    private String status;
    private LocalDateTime deliverTime;
    private LocalDateTime placedOrderTime;
    private Double productsAmount;
    private Double deliveryTax;
    private Double tipsTax;
    private String commentsSection;
    private String addressToString;
    private String cardNumber;
}
