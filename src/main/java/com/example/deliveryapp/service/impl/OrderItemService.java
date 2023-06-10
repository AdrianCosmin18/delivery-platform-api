package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.models.OrderItem;
import com.example.deliveryapp.repos.OrderItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepo orderItemRepo;


    public Map<String, Integer> getStatisticsProducts(){

        List<OrderItem> orderItems = this.orderItemRepo.findAll();

        Map<String, Integer> map = new HashMap<>();

        orderItems.forEach(orderItem -> {

            if(map.containsKey(orderItem.getProduct().getName())){
                map.put(orderItem.getProduct().getName(), map.get(orderItem.getProduct().getName()) + orderItem.getQuantity());
            }else{
                map.put(orderItem.getProduct().getName(), orderItem.getQuantity());
            }
        });

        return map;
    }
}
