package com.homebase.ecom.analytics.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_sales_summary")
public class DailySalesSummaryEntity extends BaseJpaEntity {

    @Column(name = "summary_date")
    private LocalDate summaryDate;

    @Column(name = "total_orders")
    private int totalOrders;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "total_units_sold")
    private int totalUnitsSold;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "total_tax")
    private BigDecimal totalTax;

    @Column(name = "total_shipping")
    private BigDecimal totalShipping;

    @Column(name = "avg_order_value")
    private BigDecimal avgOrderValue;

    @Column(name = "new_customers")
    private int newCustomers;

    @Column(name = "returning_customers")
    private int returningCustomers;

    @Column(name = "currency")
    private String currency;

    public LocalDate getSummaryDate() { return summaryDate; }
    public void setSummaryDate(LocalDate summaryDate) { this.summaryDate = summaryDate; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public int getTotalUnitsSold() { return totalUnitsSold; }
    public void setTotalUnitsSold(int totalUnitsSold) { this.totalUnitsSold = totalUnitsSold; }

    public BigDecimal getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(BigDecimal totalDiscount) { this.totalDiscount = totalDiscount; }

    public BigDecimal getTotalTax() { return totalTax; }
    public void setTotalTax(BigDecimal totalTax) { this.totalTax = totalTax; }

    public BigDecimal getTotalShipping() { return totalShipping; }
    public void setTotalShipping(BigDecimal totalShipping) { this.totalShipping = totalShipping; }

    public BigDecimal getAvgOrderValue() { return avgOrderValue; }
    public void setAvgOrderValue(BigDecimal avgOrderValue) { this.avgOrderValue = avgOrderValue; }

    public int getNewCustomers() { return newCustomers; }
    public void setNewCustomers(int newCustomers) { this.newCustomers = newCustomers; }

    public int getReturningCustomers() { return returningCustomers; }
    public void setReturningCustomers(int returningCustomers) { this.returningCustomers = returningCustomers; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
