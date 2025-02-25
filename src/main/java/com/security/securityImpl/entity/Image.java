package com.security.securityImpl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="imageFileUpload")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] image;

    private String description;


}
