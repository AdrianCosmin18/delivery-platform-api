package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.CourierDTO;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.Courier;
import com.example.deliveryapp.repos.CourierRepo;
import com.example.deliveryapp.service.CourierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepo courierRepo;

    @Autowired
    private ModelMapper mapper;


    @Override
    public void addCourier(CourierDTO courierDTO) {

        if(this.courierRepo.getCourierByPhone(courierDTO.getPhone()).isPresent()){
            throw new DeliveryCustomException("Already a courier with this phone");
        }

        this.courierRepo.save(this.mapper.map(courierDTO, Courier.class));
    }

    @Override
    public List<CourierDTO> getCouriers() {

        return this.courierRepo.findAll().stream().map(courier -> this.mapper.map(courier, CourierDTO.class)).collect(Collectors.toList());
    }
}
