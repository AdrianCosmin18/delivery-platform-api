package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.CityDTO;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.City;
import com.example.deliveryapp.repos.CityRepo;
import com.example.deliveryapp.service.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepo cityRepo;
    private ModelMapper mapper;

    public CityServiceImpl() {
        this.mapper = new ModelMapper();
    }

    @Override
    public void addCity(CityDTO cityDTO){
        if(this.cityRepo.getCityByName(cityDTO.getName()).isPresent()){
            throw new DeliveryCustomException("Already exists this city in db");
        }
        this.cityRepo.save(this.mapper.map(cityDTO, City.class));
    }

    @Override
    public void deleteCity(String name){
        if(this.cityRepo.getCityByName(name).isPresent()){
            this.cityRepo.deleteCityByName(name);
        }else{
            throw new DeliveryCustomException("There is no city with this name in db");
        }
    }

    @Override
    public List<CityDTO> getCities(){
        List<City> cities = this.cityRepo.findAll();
        if (cities.isEmpty()){
            return new ArrayList<>();
        }

        List<CityDTO> cityDTOList = new ArrayList<>();
        for(City c: cities){
            cityDTOList.add(this.mapper.map(c, CityDTO.class));
        }

        return cityDTOList;
    }
}
