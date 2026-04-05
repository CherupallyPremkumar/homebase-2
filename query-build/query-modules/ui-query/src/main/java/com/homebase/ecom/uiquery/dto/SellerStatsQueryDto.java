package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SellerStatsQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long activeProducts;
    private long totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageRating;
    private int productsChange;
    private int ordersChange;
    private int revenueChange;

    public long getActiveProducts() { return activeProducts; }
    public void setActiveProducts(long activeProducts) { this.activeProducts = activeProducts; }
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    public int getProductsChange() { return productsChange; }
    public void setProductsChange(int productsChange) { this.productsChange = productsChange; }
    public int getOrdersChange() { return ordersChange; }
    public void setOrdersChange(int ordersChange) { this.ordersChange = ordersChange; }
    public int getRevenueChange() { return revenueChange; }
    public void setRevenueChange(int revenueChange) { this.revenueChange = revenueChange; }
}
