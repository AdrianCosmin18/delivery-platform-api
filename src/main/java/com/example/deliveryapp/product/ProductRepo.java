package com.example.deliveryapp.product;

import com.example.deliveryapp.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.restaurant = :name")
    List<Product> getProductsByRestaurantName(@Param("name") String name);

    @Query("select p from Product p where p.restaurant = :name and p.type = :type")
    List<Product> getProductsByRestaurantNameAndType(@Param("name")String name, @Param("type")String type);

    @Query("delete from Product p where p.name = :productName and p.restaurant.name = :restaurantName")
    void deleteProductByNameAndRestaurant(@Param("productName") String productName, @Param("restaurantName") String restaurantName);

    @Query("select p from Product p where p.name = :productName and p.restaurant.name = :restaurantName")
    Optional<Product> getProductByNameAndRestaurantName(@Param("productName") String productName, @Param("restaurantName") String restaurantName);


}