package com.homebase.ecom.analytics.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductPerfQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String productId;
    private Date periodDate;
    private int views;
    private int purchases;
    private BigDecimal revenue;
    private BigDecimal avgRating;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public Date getPeriodDate() { return periodDate; }
    public void setPeriodDate(Date periodDate) { this.periodDate = periodDate; }
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    public int getPurchases() { return purchases; }
    public void setPurchases(int purchases) { this.purchases = purchases; }
    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
    public BigDecimal getAvgRating() { return avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }
}
