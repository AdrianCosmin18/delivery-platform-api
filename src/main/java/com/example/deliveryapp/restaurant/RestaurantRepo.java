package com.example.deliveryapp.restaurant;

import com.example.deliveryapp.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> getRestaurantByName(String name);
    void deleteRestaurantByName(String name);
}
