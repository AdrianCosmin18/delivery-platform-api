package com.example.deliveryapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Address")
@Table(name = "address")
public class Address {

    @Id
    @SequenceGenerator(name = "address_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    private Long id;

    @NotEmpty
    @Column(name = "street", nullable = false)
    private String street;

    @NotEmpty
    @Column(name = "number", nullable = false)
    private Integer number;

    @NotEmpty
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "city_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "city_id_fk"))
    @JsonBackReference
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_id_fk")
    )
    @JsonBackReference
    private User user;
}
