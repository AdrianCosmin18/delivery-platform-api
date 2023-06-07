package com.example.deliveryapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@Entity(name = "Product")
@ToString
@Builder
public class Product {

    @Id
    @SequenceGenerator(name = "product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "containsLactose")
    private Boolean containsLactose;

    @Column(name = "containsGluten")
    private Boolean containsGluten;

    @Column(name = "isVegetarian")
    private Boolean isVegetarian;

//    @Lob
//    @Column(name = "picture", length = 1000)
//    private byte[] picture;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "restaurant_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "restaurant_id_fk")
    )
    @JsonBackReference
    private Restaurant restaurant;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "product"
    )
    @JsonBackReference
    private List<OrderItem> orderItems;

    public void addOrderItem(OrderItem item){
        this.orderItems.add(item);
        item.setProduct(this);
    }
}
