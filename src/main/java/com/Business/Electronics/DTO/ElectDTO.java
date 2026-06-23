package com.Business.Electronics.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectDTO {

    private String url;
    private String model;
    private String condition;
    private Integer months;
    private Double price;

}
