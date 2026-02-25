package com.ecommerce.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DashboardStatsDto {
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    private String revenueGrowth = "0%";
    private int totalOrders = 0;
    private String orderGrowth = "0%";
    private BigDecimal avgOrderValue = BigDecimal.ZERO;
    private long pendingOrders = 0;
}
