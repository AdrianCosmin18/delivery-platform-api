package com.example.deliveryapp.models;

import com.example.deliveryapp.security.security.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
public class User implements UserDetails {

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

    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @NotEmpty(message = "phone number is required")
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "isAccountNonExpired")
    private boolean isAccountNonExpired = true;
    @Column(name = "isAccountNonLocked")
    private boolean isAccountNonLocked = true;
    @Column(name = "isCredentialsNonExpired")
    private boolean isCredentialsNonExpired = true;
    @Column(name = "isEnabled")
    private boolean isEnabled = true;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "wish_list",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "restaurant_id")}
    )
    @JsonManagedReference
    private List<Restaurant> restaurants;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "user")
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

    public User(String lastName, String firstName, String email, String password, String phone) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.phone = phone;
    }

    public void addAddress(Address address){

        this.addresses.add(address);
        address.setUser(this);
    }

    public void deleteAddress(Address address){
        this.addresses.remove(address);
    }

    public void addCard(Card card){

        this.cards.add(card);
        card.setUser(this);
    }

    public void deleteCard(Card card){
        this.cards.remove(card);
    }

    public void addRestaurant(Restaurant restaurant){ this.restaurants.add(restaurant); }
    public void removeRestaurant(Restaurant restaurant){ this.restaurants.remove(restaurant); }

    public void addOrder(Order order){
        this.orders.add(order);
        order.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //ii dam rolul de user creat in UserRole pe baza lui UserPermission
        return this.userRole.getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

}
