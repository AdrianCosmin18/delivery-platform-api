package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.OrderDTO;
import com.example.deliveryapp.DTOs.OrderItemDTO;
import com.example.deliveryapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("delivery-app/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public OrderDTO getOrderById(@PathVariable Long id){
        return this.orderService.getOrderById(id);
    }

    @GetMapping("/get-orderItems-by-orderId/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<OrderItemDTO> getOrderItemsByOrderId(@PathVariable Long orderId){
        return this.orderService.getOrderItemsByOrderId(orderId);
    }

    @GetMapping("/get-orders-in-payment-confirmed-state")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInPaymentConfirmedState(){
        return this.orderService.getOrdersInPaymentConfirmedState();
    }
}
