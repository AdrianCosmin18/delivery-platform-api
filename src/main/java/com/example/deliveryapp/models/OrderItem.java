package com.example.deliveryapp.models;
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

    @Id
    @SequenceGenerator(name = "orderItem_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderItem_seq")
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
