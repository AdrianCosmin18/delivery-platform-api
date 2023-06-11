package com.example.deliveryapp.address;

import com.example.deliveryapp.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

    @Query("select a from Address a where a.city.name = :cityName  and a.street = :street and a.number = :number and a.block = :block and a.staircase = :staircase and a.floor = :floor and a.apartment = :apartment")
    Optional<Address> getFullAddress(@Param("cityName")String cityName,
                                     @Param("street")String street,
                                     @Param("number")Integer number,
                                     @Param("block")String block,
                                     @Param("staircase")String staircase,
                                     @Param("floor")int floor,
                                     @Param("apartment")int apartment);

    @Query("delete from Address a where a.city.name = :cityName  and a.street = :street and a.number = :number and a.block = :block and a.staircase = :staircase and a.floor = :floor and a.apartment = :apartment")
    void deleteAddresses(@Param("cityName")String cityName,
                         @Param("street")String street,
                         @Param("number")Integer number,
                         @Param("block")String block,
                         @Param("staircase")String staircase,
                         @Param("floor")int floor,
                         @Param("apartment")int apartment);
}
