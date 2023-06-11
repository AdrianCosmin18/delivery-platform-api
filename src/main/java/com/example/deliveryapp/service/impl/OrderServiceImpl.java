package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.OrderDTO;
import com.example.deliveryapp.DTOs.OrderItemDTO;
import com.example.deliveryapp.constants.Constants;
import com.example.deliveryapp.constants.OrderStatus;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.Courier;
import com.example.deliveryapp.models.Order;
import com.example.deliveryapp.models.OrderItem;
import com.example.deliveryapp.repos.CourierRepo;
import com.example.deliveryapp.repos.OrderRepo;
import com.example.deliveryapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CourierRepo courierRepo;

    @Override
    public OrderDTO getOrderById(Long id){

        Order order = this.orderRepo.findById(id)
                .orElseThrow(() -> new DeliveryCustomException(Constants.ORDER_NOT_FOUND_BY_ID.getMessage()));


        String addressToString =
                order.getAddress().getStreet() + ", nr." +
                        order.getAddress().getNumber() + ", " +
                        order.getAddress().getCity().getName();

        String cardNumber = "***";
        if(order.getCard() != null){
            cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
        }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            return OrderDTO.builder()
                .amount(order.getAmount())
                .commentsSection(order.getCommentsSection())
                .status(order.getStatus())
                .deliverTime(order.getDeliveredTime())
                .paymentConfirmed(order.getPaymentConfirmed())
                .orderInPreparation(order.getOrderInPreparation())
                .orderInDelivery(order.getOrderInDelivery())
                .canceledOrder(order.getCanceledOrder())
                .placedOrderTime(order.getPlacedOrderTime().toString())
                .deliveryTax(order.getDeliveryTax())
                .tipsTax(order.getTipsTax())
                .productsAmount(order.getProductsAmount())
                .id(order.getId())
                .addressToString(addressToString)
                .cardNumber(cardNumber)
                .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
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

    @Override
    public List<OrderDTO> getOrdersInPlacedOrderState(){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
            criteriaBuilder.isNotNull(root.get("placedOrderTime"));


        Specification<Order> specificationPaymentConfirmedIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("paymentConfirmed"));

        Specification<Order> specificationCancelOrderIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("canceledOrder"));

        Specification<Order> combinedSpecification = Specification
                .where(specificationPlacedOrderNotNull)
                .and(specificationPaymentConfirmedIsNull)
                .and(specificationCancelOrderIsNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification);


        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);

            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getOrdersInPlacedOrderState(String cityName){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));


        Specification<Order> specificationPaymentConfirmedIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("paymentConfirmed"));

        Specification<Order> specificationCancelOrderIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("canceledOrder"));

        Specification<Order> combinedSpecification = Specification
                .where(specificationPlacedOrderNotNull)
                .and(specificationPaymentConfirmedIsNull)
                .and(specificationCancelOrderIsNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification)
                .stream()
                .filter(order -> order.getAddress().getCity().getName().equals(cityName))
                .collect(Collectors.toList());


        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);

            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public void putOrderInPaymentConfirmationState(long orderId){

        Order order = this.orderRepo.findById(orderId)
                .orElseThrow(() -> new DeliveryCustomException(Constants.ORDER_NOT_FOUND_BY_ID.getMessage()));

        if(order.getPaymentConfirmed() != null){
            throw new DeliveryCustomException("Order is not in the correct state");
        }

        order.setStatus(OrderStatus.PAYMENT_CONFIRMED);
        order.setPaymentConfirmed(LocalDateTime.now().toString());
        this.orderRepo.saveAndFlush(order);
    }

    @Override
    public List<OrderDTO> getOrdersInPaymentConfirmationState(){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));

        Specification<Order> specificationPaymentConfirmedNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("paymentConfirmed"));

        Specification<Order> specificationOrderInPrepareIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("orderInPreparation"));

        Specification<Order> specificationCancelOrderIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("canceledOrder"));

        Specification<Order> combinedSpecification =
                        Specification
                        .where(specificationPlacedOrderNotNull)
                        .and(specificationPaymentConfirmedNotNull)
                        .and(specificationOrderInPrepareIsNull)
                        .and(specificationCancelOrderIsNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification);

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getOrdersInPaymentConfirmationState(String cityName){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));

        Specification<Order> specificationPaymentConfirmedNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("paymentConfirmed"));

        Specification<Order> specificationOrderInPrepareIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("orderInPreparation"));

        Specification<Order> specificationCancelOrderIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("canceledOrder"));

        Specification<Order> combinedSpecification =
                Specification
                        .where(specificationPlacedOrderNotNull)
                        .and(specificationPaymentConfirmedNotNull)
                        .and(specificationOrderInPrepareIsNull)
                        .and(specificationCancelOrderIsNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification)
                .stream()
                .filter(order -> order.getAddress().getCity().getName().equals(cityName))
                .collect(Collectors.toList());

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public void putOrderInPreparationState(long orderId){

        Order order = this.orderRepo.findById(orderId)
                .orElseThrow(() -> new DeliveryCustomException(Constants.ORDER_NOT_FOUND_BY_ID.getMessage()));

        if(order.getPaymentConfirmed() == null || order.getOrderInPreparation() != null){
            throw new DeliveryCustomException("Order is not in the correct state");
        }

        order.setStatus(OrderStatus.ORDER_IN_PREPARATION);
        order.setOrderInPreparation(LocalDateTime.now().toString());
        this.orderRepo.saveAndFlush(order);
    }

    @Override
    public List<OrderDTO> getOrdersInPreparationState(){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));

        Specification<Order> specificationPaymentConfirmedNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("paymentConfirmed"));

        Specification<Order> specificationOrderInPrepareNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInPreparation"));

        Specification<Order> specificationOrderInDeliveryIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("orderInDelivery"));

        Specification<Order> specificationCancelOrderIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("canceledOrder"));

        Specification<Order> combinedSpecification =
                Specification
                        .where(specificationPlacedOrderNotNull)
                        .and(specificationPaymentConfirmedNotNull)
                        .and(specificationOrderInPrepareNotNull)
                        .and(specificationOrderInDeliveryIsNull)
                        .and(specificationCancelOrderIsNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification);

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getOrdersInPreparationState(String cityName){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));

        Specification<Order> specificationPaymentConfirmedNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("paymentConfirmed"));

        Specification<Order> specificationOrderInPrepareNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInPreparation"));

        Specification<Order> specificationOrderInDeliveryIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("orderInDelivery"));

        Specification<Order> specificationCancelOrderIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("canceledOrder"));

        Specification<Order> combinedSpecification =
                Specification
                        .where(specificationPlacedOrderNotNull)
                        .and(specificationPaymentConfirmedNotNull)
                        .and(specificationOrderInPrepareNotNull)
                        .and(specificationOrderInDeliveryIsNull)
                        .and(specificationCancelOrderIsNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification)
                .stream()
                .filter(order -> order.getAddress().getCity().getName().equals(cityName))
                .collect(Collectors.toList());

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public void putOrderInDeliveryState(long orderId, long courierId){

        Order order = this.orderRepo.findById(orderId)
                .orElseThrow(() -> new DeliveryCustomException(Constants.ORDER_NOT_FOUND_BY_ID.getMessage()));

        if(order.getPaymentConfirmed() == null || order.getOrderInPreparation() == null || order.getOrderInDelivery() != null){
            throw new DeliveryCustomException("Order is not in the correct state");
        }

        order.setStatus(OrderStatus.ORDER_IN_DELIVERY);
        order.setOrderInDelivery(LocalDateTime.now().toString());

        Courier courier = this.courierRepo.findById(courierId)
                        .orElseThrow(() -> new DeliveryCustomException("No courier with this id"));
        order.setCourier(courier);
        this.orderRepo.saveAndFlush(order);
    }

    @Override
    public List<OrderDTO> getOrdersInDeliveryState(){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));

        Specification<Order> specificationPaymentConfirmedNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("paymentConfirmed"));

        Specification<Order> specificationOrderInPrepareNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInPreparation"));

        Specification<Order> specificationOrderInDeliveryNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInDelivery"));

        Specification<Order> specificationOrderDeliveredIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("deliveredTime"));

        Specification<Order> specificationCancelOrderIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("canceledOrder"));

        Specification<Order> combinedSpecification =
                Specification
                        .where(specificationPlacedOrderNotNull)
                        .and(specificationPaymentConfirmedNotNull)
                        .and(specificationOrderInPrepareNotNull)
                        .and(specificationOrderInDeliveryNotNull)
                        .and(specificationOrderDeliveredIsNull)
                        .and(specificationCancelOrderIsNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification);

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getOrdersInDeliveryState(String cityName){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));

        Specification<Order> specificationPaymentConfirmedNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("paymentConfirmed"));

        Specification<Order> specificationOrderInPrepareNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInPreparation"));

        Specification<Order> specificationOrderInDeliveryNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInDelivery"));

        Specification<Order> specificationOrderDeliveredIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("deliveredTime"));

        Specification<Order> specificationCancelOrderIsNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("canceledOrder"));

        Specification<Order> combinedSpecification =
                Specification
                        .where(specificationPlacedOrderNotNull)
                        .and(specificationPaymentConfirmedNotNull)
                        .and(specificationOrderInPrepareNotNull)
                        .and(specificationOrderInDeliveryNotNull)
                        .and(specificationOrderDeliveredIsNull)
                        .and(specificationCancelOrderIsNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification)
                .stream()
                .filter(order -> order.getAddress().getCity().getName().equals(cityName))
                .collect(Collectors.toList());

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getFinalizedOrders(){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));

        Specification<Order> specificationPaymentConfirmedNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("paymentConfirmed"));

        Specification<Order> specificationOrderInPrepareNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInPreparation"));

        Specification<Order> specificationOrderInDeliveryNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInDelivery"));

        Specification<Order> specificationOrderDeliveredNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("deliveredTime"));

        Specification<Order> combinedSpecification =
                Specification
                        .where(specificationPlacedOrderNotNull)
                        .and(specificationPaymentConfirmedNotNull)
                        .and(specificationOrderInPrepareNotNull)
                        .and(specificationOrderInDeliveryNotNull)
                        .and(specificationOrderDeliveredNotNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification)
                .stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .collect(Collectors.toList());

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getFinalizedOrders(String cityName){

        Specification<Order> specificationPlacedOrderNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("placedOrderTime"));

        Specification<Order> specificationPaymentConfirmedNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("paymentConfirmed"));

        Specification<Order> specificationOrderInPrepareNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInPreparation"));

        Specification<Order> specificationOrderInDeliveryNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("orderInDelivery"));

        Specification<Order> specificationOrderDeliveredNotNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("deliveredTime"));

        Specification<Order> combinedSpecification =
                Specification
                        .where(specificationPlacedOrderNotNull)
                        .and(specificationPaymentConfirmedNotNull)
                        .and(specificationOrderInPrepareNotNull)
                        .and(specificationOrderInDeliveryNotNull)
                        .and(specificationOrderDeliveredNotNull);


        List<Order> orders = this.orderRepo.findAll(combinedSpecification)
                .stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .filter(order -> order.getAddress().getCity().getName().equals(cityName))
                .collect(Collectors.toList());

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getCanceledOrders(){

        Specification<Order> specificationCancelOrder = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("canceledOrder"));

        List<Order> orders = this.orderRepo.findAll(specificationCancelOrder)
                .stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .collect(Collectors.toList());

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getCanceledOrders(String cityName){

        Specification<Order> specificationCancelOrder = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("canceledOrder"));

        List<Order> orders = this.orderRepo.findAll(specificationCancelOrder)
                .stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .filter(order -> order.getAddress().getCity().getName().equals(cityName))
                .collect(Collectors.toList());

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){

            String addressToString =
                    order.getAddress().getStreet() + ", nr." +
                            order.getAddress().getNumber() + ", " +
                            order.getAddress().getCity().getName();

            String cardNumber = "***";
            if(order.getCard() != null){
                cardNumber += order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
//        String cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);


            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(order.getAddress().getCity().getName())
                    .cardNumber(cardNumber)
                    .username(order.getUser().getLastName() + " " + order.getUser().getFirstName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }


}
