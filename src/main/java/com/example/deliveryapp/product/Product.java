package com.example.deliveryapp.product;

import com.example.deliveryapp.image.Image;
import com.example.deliveryapp.orderItem.OrderItem;
import com.example.deliveryapp.restaurant.Restaurant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
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


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Product)) return false;
        Product product = (Product) object;
        return java.util.Objects.equals(getName(), product.getName()) && java.util.Objects.equals(getPrice(), product.getPrice()) && java.util.Objects.equals(getType(), product.getType()) && java.util.Objects.equals(getDescription(), product.getDescription()) && java.util.Objects.equals(getIngredients(), product.getIngredients()) && java.util.Objects.equals(getContainsLactose(), product.getContainsLactose()) && java.util.Objects.equals(getContainsGluten(), product.getContainsGluten()) && java.util.Objects.equals(getIsVegetarian(), product.getIsVegetarian()) && java.util.Objects.equals(getImage(), product.getImage());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getName(), getPrice(), getType(), getDescription(), getIngredients(), getContainsLactose(), getContainsGluten(), getIsVegetarian(), getImage());
    }

    public void addOrderItem(OrderItem item){
        this.orderItems.add(item);
        item.setProduct(this);
    }

    public void setImage(Image image){
        this.image = image;
        image.setProduct(this);
    }
}
