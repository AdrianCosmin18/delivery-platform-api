package com.example.deliveryapp.repos;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

    @Query("select a from Address a where a.city.name = :cityName  and a.street = :street and a.number = :number")
    Optional<Address> getFullAddress(@Param("cityName")String cityName,@Param("street")String street,@Param("number")Integer number);

    @Query("delete from Address a where a.city.name = :cityName  and a.street = :street and a.number = :number")
    void deleteAddresses(@Param("cityName")String cityName,@Param("street")String street,@Param("number")Integer number);
}
