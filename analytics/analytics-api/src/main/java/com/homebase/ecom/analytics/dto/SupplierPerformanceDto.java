package com.homebase.ecom.analytics.dto;

import java.math.BigDecimal;

public class SupplierPerformanceDto {

    private String supplierId;
    private int periodMonth;
    private int periodYear;
    private int totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal avgRating;
    private BigDecimal cancellationRate;

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public int getPeriodMonth() { return periodMonth; }
    public void setPeriodMonth(int periodMonth) { this.periodMonth = periodMonth; }

    public int getPeriodYear() { return periodYear; }
    public void setPeriodYear(int periodYear) { this.periodYear = periodYear; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getAvgRating() { return avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }

    public BigDecimal getCancellationRate() { return cancellationRate; }
    public void setCancellationRate(BigDecimal cancellationRate) { this.cancellationRate = cancellationRate; }
}
