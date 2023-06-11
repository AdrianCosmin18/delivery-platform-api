package com.example.deliveryapp.orderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
