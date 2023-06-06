package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.OrderDTO;
import com.example.deliveryapp.DTOs.OrderItemDTO;

import java.util.List;

public interface OrderService {

    OrderDTO getOrderById(Long id);

    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);
}
