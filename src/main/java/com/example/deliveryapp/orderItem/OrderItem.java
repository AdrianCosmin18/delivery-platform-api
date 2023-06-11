package com.example.deliveryapp.orderItem;
import com.example.deliveryapp.product.Product;
import com.example.deliveryapp.order.Order;
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

//    @EmbeddedId
//    private OrderItemId id;

    @Id
    @SequenceGenerator(name = "order_item_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "extraIngredients")
    private String extraIngredients;// "sos Big Mac,salata verde,rosii" - lista cu ingrediente extra

    @Column(name = "lessIngredients")
    private String lessIngredients;// "ceapa, castraveti murati" - lista cu ingredientele pe care le scoatem din burger

    //    @MapsId("ordeId")
    @ManyToOne
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "item_order_id_fk")
    )
    private Order order;

    //    @MapsId("productId")
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "item_product_id_fk")
    )
    private Product product;
}
