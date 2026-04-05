package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OverviewStatsQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long totalOrders;
    private long activeOrders;
    private BigDecimal totalRevenue;
    private long totalCustomers;
    private long publishedProducts;
    private long pendingReviewProducts;
    private long activeSuppliers;
    private long openTickets;

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public long getActiveOrders() { return activeOrders; }
    public void setActiveOrders(long activeOrders) { this.activeOrders = activeOrders; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }
    public long getPublishedProducts() { return publishedProducts; }
    public void setPublishedProducts(long publishedProducts) { this.publishedProducts = publishedProducts; }
    public long getPendingReviewProducts() { return pendingReviewProducts; }
    public void setPendingReviewProducts(long pendingReviewProducts) { this.pendingReviewProducts = pendingReviewProducts; }
    public long getActiveSuppliers() { return activeSuppliers; }
    public void setActiveSuppliers(long activeSuppliers) { this.activeSuppliers = activeSuppliers; }
    public long getOpenTickets() { return openTickets; }
    public void setOpenTickets(long openTickets) { this.openTickets = openTickets; }
}
