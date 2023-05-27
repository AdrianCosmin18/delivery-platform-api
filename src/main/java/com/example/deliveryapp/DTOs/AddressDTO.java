package com.example.deliveryapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {

    private Long id;
    private String street;
    private Integer number;
    private String block;
    private String staircase;
    private int floor;
    private int apartment;
    private String interphone;
    private String details;
    private Boolean isDefault;
    private String cityName;

    public AddressDTO(String street, Integer number, String cityName) {
        this.street = street;
        this.number = number;
        this.isDefault = false;
        this.cityName = cityName;
    }
}
