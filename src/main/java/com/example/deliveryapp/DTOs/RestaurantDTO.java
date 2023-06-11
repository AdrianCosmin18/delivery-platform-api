package com.example.deliveryapp.DTOs;

import com.example.deliveryapp.city.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private String name;
    private List<City> cities;
}
