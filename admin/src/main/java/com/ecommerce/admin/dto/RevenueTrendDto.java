package com.ecommerce.admin.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueTrendDto {
    private String date;
    private BigDecimal amount;
}
