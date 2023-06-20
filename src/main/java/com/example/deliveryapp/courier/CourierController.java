package com.example.deliveryapp.courier;

import com.example.deliveryapp.DTOs.CourierDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public CourierDTO getCourierById(@PathVariable Long id){
        return this.courierService.getCourierById(id);
    }

}
