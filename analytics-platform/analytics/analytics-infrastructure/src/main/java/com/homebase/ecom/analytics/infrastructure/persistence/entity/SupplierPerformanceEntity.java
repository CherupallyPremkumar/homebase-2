package com.homebase.ecom.analytics.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "supplier_performance")
public class SupplierPerformanceEntity extends BaseJpaEntity {

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "total_orders")
    private int totalOrders;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "total_returns")
    private int totalReturns;

    @Column(name = "avg_fulfillment_days")
    private BigDecimal avgFulfillmentDays;

    @Column(name = "avg_rating")
    private BigDecimal avgRating;

    @Column(name = "cancellation_rate")
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

    public int getTotalReturns() { return totalReturns; }
    public void setTotalReturns(int totalReturns) { this.totalReturns = totalReturns; }

    public BigDecimal getAvgFulfillmentDays() { return avgFulfillmentDays; }
    public void setAvgFulfillmentDays(BigDecimal avgFulfillmentDays) { this.avgFulfillmentDays = avgFulfillmentDays; }

    public BigDecimal getAvgRating() { return avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }

    public BigDecimal getCancellationRate() { return cancellationRate; }
    public void setCancellationRate(BigDecimal cancellationRate) { this.cancellationRate = cancellationRate; }
}
