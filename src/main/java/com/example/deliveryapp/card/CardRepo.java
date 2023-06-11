package com.example.deliveryapp.card;

import com.example.deliveryapp.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {

    Optional<Card> getCardByCardNumber(String cardNumber);
    List<Card> getCardsByCardHolderName(String cardHolderName);

    @Query("select c from Card c where c.user.email = :email")
    List<Card> getCardsByEmailUser(@Param("email") String email);
}
