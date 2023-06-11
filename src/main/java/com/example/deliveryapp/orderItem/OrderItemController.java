package com.example.deliveryapp.orderItem;

import com.example.deliveryapp.orderItem.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("delivery-app/orderItem")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/get-statistics")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Map<String, Integer> getStatisticsProducts(){
        return this.orderItemService.getStatisticsProducts();
    }
}
