package com.example.deliveryapp.repos;

import com.example.deliveryapp.models.Cart;
import com.example.deliveryapp.models.Product;
import com.example.deliveryapp.models.User;
import com.example.deliveryapp.models.embeddedKey.CartId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, CartId> {

    Optional<Cart> getCartByUserAndProduct(User user, Product product);
}
