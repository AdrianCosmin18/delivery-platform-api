package com.example.deliveryapp.courier;

import com.example.deliveryapp.order.Order;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

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


    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Order> orders;

    public void addOrder(Order order){
        this.orders.add(order);
        order.setCourier(this);
    }
}
