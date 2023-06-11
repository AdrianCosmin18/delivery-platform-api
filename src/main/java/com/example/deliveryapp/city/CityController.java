package com.example.deliveryapp.city;

import com.example.deliveryapp.DTOs.CityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("delivery-app/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping()
    public void addCity(@RequestBody CityDTO cityDTO){
        this.cityService.addCity(cityDTO);
    }

    @GetMapping
    public ResponseEntity<List<CityDTO>> getCities(){
        return new ResponseEntity<List<CityDTO>>(this.cityService.getCities(), HttpStatus.OK);
    }
}
