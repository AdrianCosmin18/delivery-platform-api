package com.example.deliveryapp.order;

import com.example.deliveryapp.DTOs.OrderDTO;
import com.example.deliveryapp.DTOs.OrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-orders-in-placed-order-state")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInPlacedOrderState(){
        return this.orderService.getOrdersInPlacedOrderState();
    }

    @GetMapping("/get-orders-in-placed-order-state/{cityName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInPlacedOrderState(@PathVariable String cityName){
        return this.orderService.getOrdersInPlacedOrderState(cityName);
    }

    @PutMapping("/put-order-in-payment-confirmation-state/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void putOrderInPaymentConfirmationState(@PathVariable long orderId){
        this.orderService.putOrderInPaymentConfirmationState(orderId);
    }

    @GetMapping("/get-orders-in-payment-confirmation-state")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInPaymentConfirmedState(){
        return this.orderService.getOrdersInPaymentConfirmationState();
    }

    @GetMapping("/get-orders-in-payment-confirmation-state/{cityName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInPaymentConfirmedState(@PathVariable String cityName){
        return this.orderService.getOrdersInPaymentConfirmationState(cityName);
    }

    @PutMapping("/put-order-in-preparation-state/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void putOrderInPreparationState(@PathVariable long orderId){
        this.orderService.putOrderInPreparationState(orderId);
    }

    @GetMapping("/get-orders-in-preparation-state")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInPreparationState(){
        return this.orderService.getOrdersInPreparationState();
    }

    @GetMapping("/get-orders-in-preparation-state/{cityName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInPreparationState(@PathVariable String cityName){
        return this.orderService.getOrdersInPreparationState(cityName);
    }

    @PutMapping("/put-order-in-delivery-state/{orderId}/{courierId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void putOrderInDeliveryStateToCourier(@PathVariable long orderId, @PathVariable long courierId){
        this.orderService.putOrderInDeliveryState(orderId, courierId);
    }

    @GetMapping("/get-orders-in-delivery-state")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInDeliveryState(){
        return this.orderService.getOrdersInDeliveryState();
    }

    @GetMapping("/get-orders-in-delivery-state/{cityName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getOrdersInDeliveryState(@PathVariable String cityName){
        return this.orderService.getOrdersInDeliveryState(cityName);
    }


    @GetMapping("/get-finalized-orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getFinalizedOrders(){
        return this.orderService.getFinalizedOrders();
    }

    @GetMapping("/get-finalized-orders/{cityName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getFinalizedOrders(@PathVariable String cityName){
        return this.orderService.getFinalizedOrders(cityName);
    }

    @GetMapping("/get-canceled-orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getCanceledOrders(){
        return this.orderService.getCanceledOrders();
    }

    @GetMapping("/get-canceled-orders/{cityName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrderDTO> getCanceledOrders(@PathVariable String cityName){
        return this.orderService.getCanceledOrders(cityName);
    }

}
