package com.example.deliveryapp.repos;

import com.example.deliveryapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.restaurant = :name")
    List<Product> getProductsByRestaurantName(@Param("name") String name);

    @Query("select p from Product p where p.restaurant = :name and p.type = :type")
    List<Product> getProductsByRestaurantNameAndType(@Param("name")String name, @Param("type")String type);
}