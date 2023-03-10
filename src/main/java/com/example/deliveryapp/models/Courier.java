package com.example.deliveryapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Courier")
@Table(name = "courier")
public class Courier {

    @Id
    @SequenceGenerator(name = "courier_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courier_seq")
    private Long id;

    @NotEmpty
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotEmpty
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotEmpty
    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;
}
