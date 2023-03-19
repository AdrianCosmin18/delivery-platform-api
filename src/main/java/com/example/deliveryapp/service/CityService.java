package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.CityDTO;
import com.example.deliveryapp.models.City;

import java.util.List;

public interface CityService {

    void addCity(CityDTO cityDTO);
    void deleteCity(String name);
    List<CityDTO> getCities();
}
