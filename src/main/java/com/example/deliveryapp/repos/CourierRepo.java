package com.example.deliveryapp.repos;

import com.example.deliveryapp.models.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierRepo extends JpaRepository<Courier, Long> {

    Optional<Courier> getCourierByPhone(String phone);
}
