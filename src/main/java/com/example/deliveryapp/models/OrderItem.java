package com.example.deliveryapp.models;
import com.example.deliveryapp.models.embeddedKey.OrderItemId;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
@Entity(name = "OrderItem")
@ToString
@Builder
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(
            name = "order_id",
            foreignKey = @ForeignKey(name = "item_order_id_fk")
    )
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "item_product_id_fk")
    )
    private Product product;
}
