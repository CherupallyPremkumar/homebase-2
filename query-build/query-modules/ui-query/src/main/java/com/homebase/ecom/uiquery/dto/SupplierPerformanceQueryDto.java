package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SupplierPerformanceQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String supplierId;
    private String businessName;
    private String supplierState;
    private long activeOffers;
    private long totalOrders;
    private BigDecimal totalRevenue;

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getSupplierState() { return supplierState; }
    public void setSupplierState(String supplierState) { this.supplierState = supplierState; }
    public long getActiveOffers() { return activeOffers; }
    public void setActiveOffers(long activeOffers) { this.activeOffers = activeOffers; }
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}
