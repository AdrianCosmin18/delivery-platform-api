package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.models.Address;
import com.example.deliveryapp.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("delivery-app/address")
public class AddressController {

    @Autowired
    private AddressService addressService;


    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        return new ResponseEntity<List<AddressDTO>>(this.addressService.getAddresses(), HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteAddresses(@RequestBody AddressDTO addressDTO){
        this.addressService.deleteAddresses(addressDTO);
    }
}
