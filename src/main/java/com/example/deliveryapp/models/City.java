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
@Entity(name = "City")
@Table(name = "city")
public class City {

    @Id
    @SequenceGenerator(name = "city_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_seq")
    private Long id;

    @NotEmpty(message = "Name of the city can't be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty(message = "Name of the country can't be empty")
    @Column(name = "country", nullable = false)
    private String country;


    @ManyToMany(mappedBy = "cities", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Restaurant> restaurants;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "city"
    )
    @JsonBackReference
    private List<Address> addresses;

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }
}
