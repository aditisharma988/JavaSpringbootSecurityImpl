package com.security.securityImpl.entity.csv;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "industry_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IndustryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;

    private String industryCodeANZSIC;

    private String industryNameANZSIC;

    private String rmeSizeGrp;

    private String variable;

    private String value;

    private String unit;

}


