package com.Business.Electronics.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProDTO {

    private String productName;
    private Double price;
    private String category;
    private String description;
    private YearMonth monthStarted;
    private String imageUrl;
    private String publicId;
    private String brand;

}
