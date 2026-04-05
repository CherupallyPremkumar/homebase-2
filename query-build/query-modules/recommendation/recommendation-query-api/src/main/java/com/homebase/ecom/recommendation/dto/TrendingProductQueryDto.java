package com.homebase.ecom.recommendation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TrendingProductQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String productId;
    private String category;
    private BigDecimal trendingScore;
    private String timeWindow;
    private int viewCount;
    private int orderCount;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getTrendingScore() { return trendingScore; }
    public void setTrendingScore(BigDecimal trendingScore) { this.trendingScore = trendingScore; }
    public String getTimeWindow() { return timeWindow; }
    public void setTimeWindow(String timeWindow) { this.timeWindow = timeWindow; }
    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }
    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
