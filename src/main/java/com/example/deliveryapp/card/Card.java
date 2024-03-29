package com.example.deliveryapp.card;

import com.example.deliveryapp.order.Order;
import com.example.deliveryapp.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Card")
@Table(name = "card")
public class Card {

    @Id
    @SequenceGenerator(name = "card_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
    private Long id;

    @NotEmpty
    @Column(name = "card_type", nullable = false)
    private String cardType;

    @NotEmpty
    @Column(name = "card_number", nullable = false)
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotEmpty
    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    @NotEmpty
    @Column(name = "security_code", nullable = false)
    @Size(min = 3, max = 3)
    private String securityCode;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @NotNull
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_id_fk")
    )
    @JsonBackReference
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "card")
    @JsonBackReference
    private List<Order> orders;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(getCardNumber(), card.getCardNumber()) && Objects.equals(getCardHolderName(), card.getCardHolderName()) && Objects.equals(getSecurityCode(), card.getSecurityCode()) && Objects.equals(getExpiryDate(), card.getExpiryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardNumber(), getCardHolderName(), getSecurityCode(), getExpiryDate());
    }

    public void addOrder(Order order){
        this.orders.add(order);
        order.setCard(this);
    }

    public void deleteOrder(Order order){
        this.orders.remove(order);
        order.setCard(null);
    }
}
