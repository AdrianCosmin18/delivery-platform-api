package com.example.deliveryapp.repos;

import com.example.deliveryapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("select o from Order o where o.user.email = :email")
    Optional<Order> getOrderByUserEmail(@Param("email")String email);

    @Query("select o from Order o where o.paymentConfirmed is null")
    List<Order> getOrderInPaymentConfirmationState();
}
