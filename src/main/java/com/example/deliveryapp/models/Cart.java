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
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("productId")
    private Product product;

}
