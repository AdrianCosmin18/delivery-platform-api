package com.example.deliveryapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierDTO {

    private String fullName;
    private String phone;
    private String vehicleType;
}
