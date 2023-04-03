package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.CourierDTO;

import java.util.List;

public interface CourierService {

    void addCourier(CourierDTO courierDTO);
    List<CourierDTO> getCouriers();
}
