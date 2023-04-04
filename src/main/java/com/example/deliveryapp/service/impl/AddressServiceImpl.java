package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.constants.Constants;
import com.example.deliveryapp.constants.StatusAddress;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.Address;
import com.example.deliveryapp.models.City;
import com.example.deliveryapp.repos.AddressRepo;
import com.example.deliveryapp.repos.CityRepo;
import com.example.deliveryapp.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private CityRepo cityRepo;

    @Override
    public void deleteAddresses(AddressDTO addressDTO) {

        if(this.addressRepo.getFullAddress(addressDTO.getCityName(), addressDTO.getStreet(), addressDTO.getNumber()).isEmpty()){
            throw new DeliveryCustomException(Constants.ADDRESS_NOT_FOUND_EXCEPTION.getMessage());
        }
        this.addressRepo.deleteAddresses(addressDTO.getCityName(), addressDTO.getStreet(), addressDTO.getNumber());
    }

    public List<AddressDTO> getAddresses(){
        List<Address> addresses = this.addressRepo.findAll();
        if(addresses.isEmpty()){
            return new ArrayList<>();
        }

        List<AddressDTO> addressDTOList = new ArrayList<>();
        for(Address address: addresses){

            AddressDTO addressDTO = AddressDTO.builder()
                    .street(address.getStreet())
                    .number(address.getNumber())
                    .cityName(address.getCity().getName())
                    .build();

            addressDTOList.add(addressDTO);
        }

        return addressDTOList;
    }

}
