package com.example.deliveryapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

//    @NotEmpty
    private String lastName;
//    @NotEmpty
    private String firstName;
//    @NotEmpty
    private String email;
//    @NotEmpty(message = "phone number is required")
    private String phone;
    private String password;
}
