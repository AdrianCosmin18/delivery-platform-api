package com.example.deliveryapp.order;

import com.example.deliveryapp.DTOs.OrderDTO;
import com.example.deliveryapp.DTOs.OrderItemDTO;

import java.util.List;

public interface OrderService {

    OrderDTO getOrderById(Long id);

    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);

    List<OrderDTO> getOrdersInPlacedOrderState();

    List<OrderDTO> getOrdersInPlacedOrderState(String cityName);

    void putOrderInPaymentConfirmationState(long orderId);

    List<OrderDTO> getOrdersInPaymentConfirmationState();

    List<OrderDTO> getOrdersInPaymentConfirmationState(String cityName);

    void putOrderInPreparationState(long orderId);

    List<OrderDTO> getOrdersInPreparationState();

    List<OrderDTO> getOrdersInPreparationState(String cityName);

    void putOrderInDeliveryState(long orderId, long courierId);

    List<OrderDTO> getOrdersInDeliveryState();

    List<OrderDTO> getOrdersInDeliveryState(String cityName);

    List<OrderDTO> getFinalizedOrders();

    List<OrderDTO> getFinalizedOrders(String cityName);

    List<OrderDTO> getCanceledOrders();

    List<OrderDTO> getCanceledOrders(String cityName);
}
