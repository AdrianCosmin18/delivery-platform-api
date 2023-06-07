package com.example.deliveryapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Order")
@Table(name = "orders")
public class Order {

    @Id
    @SequenceGenerator(name = "order_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "status", nullable = false)
    private String status;//deliverd / placed / in_progress

    @Column(name = "placedOrderTime", nullable = false)
    private LocalDateTime placedOrderTime;

    @Column(name = "paymentConfirmed")
    private String paymentConfirmed;

    @Column(name = "orderInPreparation")
    private String orderInPreparation;

    @Column(name = "orderInDelivery")
    private String orderInDelivery;

    @Column(name = "canceledOrder")
    private String canceledOrder;

    @Column(name = "deliver_time", nullable = true)
    private String deliveredTime;

    @Column(name = "productsAmount", nullable = false)
    private Double productsAmount;

    @Column(name = "deliveryTax")
    private Double deliveryTax;

    @Column(name = "tipsTax")
    private Double tipsTax;

    @Column(name = "commentsSection")
    private String commentsSection;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_id_fk")
    )
    @JsonBackReference
    private User user;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "card_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "card_id_fk")
    )
    @JsonManagedReference
    private Card card;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "address_id_fk")
    )
    @JsonManagedReference
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "courier_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "courier_id_fk")
    )
    @JsonManagedReference
    private Courier courier;


    public Order(Double amount, String status, Double productsAmount, LocalDateTime placedOrderTime) {
        this.amount = amount;
        this.status = status;
        this.productsAmount = productsAmount;
        this.placedOrderTime = placedOrderTime;
    }

    public void addOrderItem(OrderItem item){
        item.setOrder(this);
        this.orderItems.add(item);
    }
}
