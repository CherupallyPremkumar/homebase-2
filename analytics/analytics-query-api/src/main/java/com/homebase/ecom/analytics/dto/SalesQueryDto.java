package com.homebase.ecom.analytics.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SalesQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date summaryDate;
    private int totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal avgOrderValue;
    private String currency;

    public Date getSummaryDate() { return summaryDate; }
    public void setSummaryDate(Date summaryDate) { this.summaryDate = summaryDate; }
    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public BigDecimal getAvgOrderValue() { return avgOrderValue; }
    public void setAvgOrderValue(BigDecimal avgOrderValue) { this.avgOrderValue = avgOrderValue; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
