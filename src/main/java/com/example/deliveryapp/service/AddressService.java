package com.example.deliveryapp.service;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.constants.StatusAddress;
import com.example.deliveryapp.exceptions.AddressAlreadyExistsException;

import java.util.List;

public interface AddressService {

    void deleteAddresses(AddressDTO addressDTO);
    List<AddressDTO> getAddresses();
}
