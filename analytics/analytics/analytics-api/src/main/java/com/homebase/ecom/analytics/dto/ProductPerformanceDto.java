package com.homebase.ecom.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductPerformanceDto {

    private String productId;
    private LocalDate periodDate;
    private int views;
    private int purchases;
    private BigDecimal revenue;
    private BigDecimal avgRating;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public LocalDate getPeriodDate() { return periodDate; }
    public void setPeriodDate(LocalDate periodDate) { this.periodDate = periodDate; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getPurchases() { return purchases; }
    public void setPurchases(int purchases) { this.purchases = purchases; }

    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }

    public BigDecimal getAvgRating() { return avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }
}
