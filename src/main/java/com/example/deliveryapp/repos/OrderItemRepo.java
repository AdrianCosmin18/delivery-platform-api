package com.example.deliveryapp.repos;

import com.example.deliveryapp.models.OrderItem;
import com.example.deliveryapp.models.embeddedKey.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, OrderItemId> {

}
