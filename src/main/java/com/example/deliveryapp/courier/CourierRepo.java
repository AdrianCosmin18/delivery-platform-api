package com.example.deliveryapp.courier;

import com.example.deliveryapp.courier.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierRepo extends JpaRepository<Courier, Long> {

    Optional<Courier> getCourierByPhone(String phone);
}
