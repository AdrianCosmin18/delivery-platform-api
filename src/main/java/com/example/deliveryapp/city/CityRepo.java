package com.example.deliveryapp.city;

import com.example.deliveryapp.city.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepo extends JpaRepository<City, Long> {

    Optional<City> getCityByName(String name);
    void deleteCityByName(String name);
}
