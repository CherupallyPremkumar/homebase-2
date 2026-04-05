package com.homebase.ecom.seller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SellerPerformanceQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sellerId;
    private int orderCount;
    private BigDecimal returnRate;
    private BigDecimal rating;

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
    public BigDecimal getReturnRate() { return returnRate; }
    public void setReturnRate(BigDecimal returnRate) { this.returnRate = returnRate; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
}
