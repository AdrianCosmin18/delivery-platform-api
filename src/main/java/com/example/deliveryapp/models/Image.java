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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Image)) return false;
        Image image = (Image) object;
        return java.util.Objects.equals(getName(), image.getName()) && java.util.Objects.equals(getFileType(), image.getFileType()) && java.util.Arrays.equals(getData(), image.getData());
    }

    @Override
    public int hashCode() {
        int result = java.util.Objects.hash(getName(), getFileType());
        result = 31 * result + java.util.Arrays.hashCode(getData());
        return result;
    }
}
