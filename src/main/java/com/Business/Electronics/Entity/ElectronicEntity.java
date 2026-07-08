package com.Business.Electronics.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "Electronics")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectronicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String publicId;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private String productType;

    @Column(name = "product_condition")
    private String condition;

    private YearMonth monthStarted;

    @Column(nullable = false)
    private Double price;

    private Boolean available = true;

    @CreationTimestamp
    private LocalDate createdAt;

}
