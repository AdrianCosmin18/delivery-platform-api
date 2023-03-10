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

    @Column(name = "deliver_time")
    private LocalDateTime deliverTime;


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
}
