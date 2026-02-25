package com.ecommerce.admin.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProductDto {
    private String name;
    private int sales;
    private int percentage;
}
