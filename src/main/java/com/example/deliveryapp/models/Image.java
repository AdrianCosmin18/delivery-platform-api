package com.example.deliveryapp.models;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
@Entity(name = "Image")
@ToString
@Builder
public class Image {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String fileType;
    private byte[] data;
    @OneToOne(mappedBy = "image", cascade = CascadeType.ALL)
    private Product product;
}
