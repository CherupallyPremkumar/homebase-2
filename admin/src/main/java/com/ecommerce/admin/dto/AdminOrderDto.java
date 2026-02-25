package com.ecommerce.admin.dto;

import lombok.Data;

@Data
public class AdminOrderDto {
    private String id;
    // Basic wrapper to map if needed, otherwise Controller uses shared Order type
}
