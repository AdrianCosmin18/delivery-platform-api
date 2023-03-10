package com.example.deliveryapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Card")
@Table(name = "card")
public class Card {

    @Id
    @SequenceGenerator(name = "card_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
    private Long id;

    @NotEmpty
    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @NotEmpty
    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    @NotEmpty
    @Column(name = "security_code", nullable = false)
    private String securityCode;

    @NotEmpty
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;



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
    private List<Order> order;
}
