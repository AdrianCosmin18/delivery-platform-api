package com.example.deliveryapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
public class User {

    @Id
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @NotEmpty
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @NotEmpty(message = "phone number is required")
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "wish_list",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "restaurant_id")}
    )
    @JsonManagedReference
    private List<Restaurant> restaurants;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_address",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "address_id")}
    )
    @JsonManagedReference
    private List<Address> addresses;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<Card> cards;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<Order> orders;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<Cart> productCart;


    public void addAddress(Address address){
        this.addresses.add(address);
    }

    public void deleteAddress(Address address){
        this.addresses.remove(address);
    }

    public void addCard(Card card){
        this.cards.add(card);
    }

    public void deleteCard(Card card){
        this.cards.remove(card);
    }

    public void addRestaurant(Restaurant restaurant){ this.restaurants.add(restaurant); }
    public void removeRestaurant(Restaurant restaurant){ this.restaurants.remove(restaurant); }

}
