package com.example.deliveryapp.courier;

import com.example.deliveryapp.DTOs.CourierDTO;

import java.util.List;

public interface CourierService {

    void addCourier(CourierDTO courierDTO);
    List<CourierDTO> getCouriers();
}
