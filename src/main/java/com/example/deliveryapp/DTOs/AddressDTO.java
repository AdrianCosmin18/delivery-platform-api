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

    private String street;
    private Integer number;
    private Boolean isDefault;
    private String cityName;

    public AddressDTO(String street, Integer number, String cityName) {
        this.street = street;
        this.number = number;
        this.isDefault = false;
        this.cityName = cityName;
    }
}
