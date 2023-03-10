package com.example.deliveryapp.models.embeddedKey;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@Embeddable
public class CartId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;


    public CartId(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }


    public CartId() {}
}
