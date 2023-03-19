package com.example.deliveryapp.repos;

import com.example.deliveryapp.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> getRestaurantByName(String name);
    void deleteRestaurantByName(String name);
}
