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
    private String placedOrderTime;
    private String paymentConfirmed;
    private String orderInPreparation;
    private String orderInDelivery;
    private String canceledOrder;
    private String deliverTime;
    private Double productsAmount;
    private Double deliveryTax;
    private Double tipsTax;
    private String commentsSection;
    private String addressToString;
    private String cardNumber;
}
