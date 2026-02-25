package com.ecommerce.admin.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class RevenueReportDto {
    private BigDecimal totalSales;
    private int totalOrders;
    private BigDecimal avgOrderValue;
    private int totalCustomers;
    private List<DailySalesDto> dailyData;

    @Data
    public static class DailySalesDto {
        private java.time.LocalDate date;
        private int orderCount;
        private BigDecimal sales;
        private BigDecimal avgValue;
    }
}
