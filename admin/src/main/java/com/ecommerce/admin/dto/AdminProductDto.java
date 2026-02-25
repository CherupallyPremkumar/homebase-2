package com.ecommerce.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AdminProductDto {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private Boolean active;
    private String status;
}
