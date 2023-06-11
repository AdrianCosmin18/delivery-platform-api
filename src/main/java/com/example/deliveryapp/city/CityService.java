package com.example.deliveryapp.city;

import com.example.deliveryapp.DTOs.CityDTO;

import java.util.List;

public interface CityService {

    void addCity(CityDTO cityDTO);
    void deleteCity(String name);
    List<CityDTO> getCities();
}
