package com.homebase.ecom.analytics.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductPerformance {

    private String id;
    private String productId;
    private LocalDate periodDate;
    private int views;
    private int addToCartCount;
    private int purchases;
    private int unitsSold;
    private BigDecimal revenue;
    private int returnsCount;
    private BigDecimal avgRating;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public LocalDate getPeriodDate() { return periodDate; }
    public void setPeriodDate(LocalDate periodDate) { this.periodDate = periodDate; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getAddToCartCount() { return addToCartCount; }
    public void setAddToCartCount(int addToCartCount) { this.addToCartCount = addToCartCount; }

    public int getPurchases() { return purchases; }
    public void setPurchases(int purchases) { this.purchases = purchases; }

    public int getUnitsSold() { return unitsSold; }
    public void setUnitsSold(int unitsSold) { this.unitsSold = unitsSold; }

    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }

    public int getReturnsCount() { return returnsCount; }
    public void setReturnsCount(int returnsCount) { this.returnsCount = returnsCount; }

    public BigDecimal getAvgRating() { return avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }
}
