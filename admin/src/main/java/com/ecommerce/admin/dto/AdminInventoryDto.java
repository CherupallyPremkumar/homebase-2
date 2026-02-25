package com.ecommerce.admin.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO that enriches InventoryItem with its associated Product details.
 */
@Data
public class AdminInventoryDto {
    private String id;
    private String productId;
    private String productName;
    private String productCategory;
    private String productImageUrl;
    private BigDecimal productPrice;
    private Integer quantity;
    private Integer reserved;
    private Integer availableQuantity;
    private Integer lowStockThreshold;
}
