package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.OrderDTO;
import com.example.deliveryapp.DTOs.OrderItemDTO;

import java.util.List;

public interface OrderService {

    OrderDTO getOrderById(Long id);

    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);

    List<OrderDTO> getOrdersInPlacedOrderState();

    void putOrderInPaymentConfirmationState(long orderId);

    List<OrderDTO> getOrdersInPaymentConfirmationState();

    void putOrderInPreparationState(long orderId);

    List<OrderDTO> getOrdersInPreparationState();

    void putOrderInDeliveryState(long orderId, long courierId);

    List<OrderDTO> getOrdersInDeliveryState();

    List<OrderDTO> getFinalizedOrders();

    List<OrderDTO> getCanceledOrders();
}
