package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.CourierDTO;
import com.example.deliveryapp.service.CourierService;
import com.example.deliveryapp.service.impl.CourierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("delivery-app/courier")
public class CourierController {


    @Autowired
    private CourierService courierService;

    @PostMapping
    public void addCourier(@RequestBody CourierDTO courierDTO){
        this.courierService.addCourier(courierDTO);
    }

    @GetMapping()
    public List<CourierDTO> getCouriers(){
        return this.courierService.getCouriers();
    }


}
