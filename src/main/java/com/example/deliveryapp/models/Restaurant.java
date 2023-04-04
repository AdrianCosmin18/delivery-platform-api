package com.example.deliveryapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Restaurant")
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @SequenceGenerator(name = "restaurant_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_seq")
    private Long id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "city_restaurant",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "city_id")})
    @JsonManagedReference
    private List<City> cities;

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Product> products;

    @ManyToMany(mappedBy = "restaurants", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<User> users;


    public Restaurant(String name) {
        this.name = name;
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.setRestaurant(this);
    }

    public void deleteProduct(Product product){
        this.products.remove(product);
    }

    public void addCity(City city){ this.cities.add(city); }

    public void removeFromCity(City city){ this.cities.remove(city); }
}
