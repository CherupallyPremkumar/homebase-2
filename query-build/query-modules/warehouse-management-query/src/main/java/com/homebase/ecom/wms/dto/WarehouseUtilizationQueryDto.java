package com.homebase.ecom.wms.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class WarehouseUtilizationQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseType;
    private String city;
    private String state;
    private long maxCapacityUnits;
    private BigDecimal currentUtilizationPct;
    private boolean isActive;
    private long totalLocations;
    private long activeLocations;
    private long totalLocationCapacity;
    private long usedLocationCapacity;
    private long totalSkus;
    private long totalUnits;
    private long lowStockCount;
    private long pendingFulfillments;

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseType() { return warehouseType; }
    public void setWarehouseType(String warehouseType) { this.warehouseType = warehouseType; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public long getMaxCapacityUnits() { return maxCapacityUnits; }
    public void setMaxCapacityUnits(long maxCapacityUnits) { this.maxCapacityUnits = maxCapacityUnits; }
    public BigDecimal getCurrentUtilizationPct() { return currentUtilizationPct; }
    public void setCurrentUtilizationPct(BigDecimal currentUtilizationPct) { this.currentUtilizationPct = currentUtilizationPct; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public long getTotalLocations() { return totalLocations; }
    public void setTotalLocations(long totalLocations) { this.totalLocations = totalLocations; }
    public long getActiveLocations() { return activeLocations; }
    public void setActiveLocations(long activeLocations) { this.activeLocations = activeLocations; }
    public long getTotalLocationCapacity() { return totalLocationCapacity; }
    public void setTotalLocationCapacity(long totalLocationCapacity) { this.totalLocationCapacity = totalLocationCapacity; }
    public long getUsedLocationCapacity() { return usedLocationCapacity; }
    public void setUsedLocationCapacity(long usedLocationCapacity) { this.usedLocationCapacity = usedLocationCapacity; }
    public long getTotalSkus() { return totalSkus; }
    public void setTotalSkus(long totalSkus) { this.totalSkus = totalSkus; }
    public long getTotalUnits() { return totalUnits; }
    public void setTotalUnits(long totalUnits) { this.totalUnits = totalUnits; }
    public long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(long lowStockCount) { this.lowStockCount = lowStockCount; }
    public long getPendingFulfillments() { return pendingFulfillments; }
    public void setPendingFulfillments(long pendingFulfillments) { this.pendingFulfillments = pendingFulfillments; }
}
