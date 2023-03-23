package com.example.deliveryapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Address")
@Table(name = "address")
@Builder
public class Address{

    @Id
    @SequenceGenerator(name = "address_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    private Long id;

    @NotEmpty
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "number", nullable = false)
    private Integer number;

    @NotNull
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

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Order> orders;

    public Address(String street, Integer number, Boolean isDefault, City city) {
        this.street = street;
        this.number = number;
        this.isDefault = isDefault;
        this.city = city;
    }

    public Address(String street, Integer number, Boolean isDefault) {
        this.street = street;
        this.number = number;
        this.isDefault = isDefault;
    }

    public Address(String street, Integer number) {
        this.street = street;
        this.number = number;
    }

    public boolean compare(Address otherAddress){
        return this.street.equals(otherAddress.street) &&
                this.number.equals(otherAddress.number) &&
                this.city.getName().equals(otherAddress.getCity().getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getNumber(), address.getNumber()) && Objects.equals(getCity(), address.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getNumber(), getCity());
    }
}
