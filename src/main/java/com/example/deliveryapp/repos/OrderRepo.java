package com.example.deliveryapp.repos;

import com.example.deliveryapp.models.Card;
import com.example.deliveryapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("select o from Order o where o.user.email = :email")
    Optional<Order> getOrderByUserEmail(@Param("email")String email);

    @Query("select o from Order o where o.paymentConfirmed is null")
    List<Order> getOrderInPaymentConfirmationState();

    @Query("UPDATE Order o SET o.card = null WHERE o.card.id = :cardId")
    @Modifying
    void updateOrderByCardId(@Param("cardId") Long cardId);
}
