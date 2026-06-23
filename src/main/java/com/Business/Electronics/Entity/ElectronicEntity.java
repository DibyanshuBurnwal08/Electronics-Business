package com.Business.Electronics.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

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

    @Column(nullable = false)
    private String model;

    @Column(name = "product_condition")
    private String condition;

    private Integer months;

    @Column(nullable = false)
    private Double price;

    @CreationTimestamp
    private LocalDate createdAt;

}
