package com.homebase.ecom.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailySalesDto {

    private LocalDate summaryDate;
    private int totalOrders;
    private BigDecimal totalRevenue;
    private int totalUnitsSold;
    private BigDecimal avgOrderValue;
    private String currency;

    public LocalDate getSummaryDate() { return summaryDate; }
    public void setSummaryDate(LocalDate summaryDate) { this.summaryDate = summaryDate; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public int getTotalUnitsSold() { return totalUnitsSold; }
    public void setTotalUnitsSold(int totalUnitsSold) { this.totalUnitsSold = totalUnitsSold; }

    public BigDecimal getAvgOrderValue() { return avgOrderValue; }
    public void setAvgOrderValue(BigDecimal avgOrderValue) { this.avgOrderValue = avgOrderValue; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
