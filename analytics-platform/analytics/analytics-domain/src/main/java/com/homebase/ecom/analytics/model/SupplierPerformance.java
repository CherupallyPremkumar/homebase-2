package com.homebase.ecom.analytics.model;

import java.math.BigDecimal;

public class SupplierPerformance {

    private String id;
    private String supplierId;
    private int periodMonth;
    private int periodYear;
    private int totalOrders;
    private BigDecimal totalRevenue;
    private int totalReturns;
    private BigDecimal avgFulfillmentDays;
    private BigDecimal avgRating;
    private BigDecimal cancellationRate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public int getTotalReturns() { return totalReturns; }
    public void setTotalReturns(int totalReturns) { this.totalReturns = totalReturns; }

    public BigDecimal getAvgFulfillmentDays() { return avgFulfillmentDays; }
    public void setAvgFulfillmentDays(BigDecimal avgFulfillmentDays) { this.avgFulfillmentDays = avgFulfillmentDays; }

    public BigDecimal getAvgRating() { return avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }

    public BigDecimal getCancellationRate() { return cancellationRate; }
    public void setCancellationRate(BigDecimal cancellationRate) { this.cancellationRate = cancellationRate; }

    private String tenant;
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
