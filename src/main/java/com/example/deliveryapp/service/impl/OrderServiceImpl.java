package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.OrderDTO;
import com.example.deliveryapp.DTOs.OrderItemDTO;
import com.example.deliveryapp.constants.Constants;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.Order;
import com.example.deliveryapp.models.OrderItem;
import com.example.deliveryapp.repos.OrderRepo;
import com.example.deliveryapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public OrderDTO getOrderById(Long id){

        Order order = this.orderRepo.findById(id)
                .orElseThrow(() -> new DeliveryCustomException(Constants.ORDER_NOT_FOUND_BY_ID.getMessage()));


        String addressToString =
                order.getAddress().getStreet() + ", nr." +
                        order.getAddress().getNumber() + ", " +
                        order.getAddress().getCity().getName();

        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


        return OrderDTO.builder()
                .amount(order.getAmount())
                .commentsSection(order.getCommentsSection())
                .status(order.getStatus())
                .deliverTime(order.getDeliveredTime().toString())
                .placedOrderTime(order.getPlacedOrderTime().toString())
                .deliveryTax(order.getDeliveryTax())
                .tipsTax(order.getTipsTax())
                .productsAmount(order.getProductsAmount())
                .id(order.getId())
                .addressToString(addressToString)
                .cardNumber(cardNumber)
                .build();
    }

    @Override
    public List<OrderItemDTO> getOrderItemsByOrderId(Long orderId){

        Order order = this.orderRepo.findById(orderId)
                .orElseThrow(() -> new DeliveryCustomException(Constants.ORDER_NOT_FOUND_BY_ID.getMessage()));

        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for(OrderItem oi: orderItems){

            OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                    .price(oi.getPrice())
                    .quantity(oi.getQuantity())
                    .extraIngredients(oi.getExtraIngredients())
                    .lessIngredients(oi.getLessIngredients())
                    .productName(oi.getProduct().getName())
                    .restaurantName(oi.getProduct().getRestaurant().getName())
                    .build();

            orderItemDTOS.add(orderItemDTO);
        }
        return orderItemDTOS;
    }
}
