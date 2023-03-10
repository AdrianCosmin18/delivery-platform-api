package com.example.deliveryapp.models;

import com.example.deliveryapp.models.embeddedKey.CartId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
@Entity(name = "Cart")
public class Cart {


    @EmbeddedId
    private CartId id;

    @Column(name = "name", nullable = false)
    private String quantity;


    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "cart_user_id_pk")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "cart_product_id_pk")
    )
    private Product product;

}
