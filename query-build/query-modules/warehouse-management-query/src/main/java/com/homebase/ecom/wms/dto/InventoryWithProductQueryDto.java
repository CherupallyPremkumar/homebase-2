package com.homebase.ecom.wms.dto;

import java.io.Serializable;

public class InventoryWithProductQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String inventoryId;
    private String sku;
    private String productId;
    private String variantId;
    private int totalQuantity;
    private int availableQuantity;
    private int reservedQuantity;
    private int damagedQuantity;
    private int inboundQuantity;
    private int lowStockThreshold;
    private String inventoryStatus;
    private String stateId;
    private String warehouseId;
    private String productName;
    private String brand;
    private String productState;
    private String categoryName;
    private String warehouseName;
    private String warehouseCode;
    private String warehouseCity;
    private boolean isLowStock;
    private boolean isOutOfStock;

    public String getInventoryId() { return inventoryId; }
    public void setInventoryId(String inventoryId) { this.inventoryId = inventoryId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }
    public int getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; }
    public int getDamagedQuantity() { return damagedQuantity; }
    public void setDamagedQuantity(int damagedQuantity) { this.damagedQuantity = damagedQuantity; }
    public int getInboundQuantity() { return inboundQuantity; }
    public void setInboundQuantity(int inboundQuantity) { this.inboundQuantity = inboundQuantity; }
    public int getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(int lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
    public String getInventoryStatus() { return inventoryStatus; }
    public void setInventoryStatus(String inventoryStatus) { this.inventoryStatus = inventoryStatus; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getProductState() { return productState; }
    public void setProductState(String productState) { this.productState = productState; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getWarehouseCity() { return warehouseCity; }
    public void setWarehouseCity(String warehouseCity) { this.warehouseCity = warehouseCity; }
    public boolean getIsLowStock() { return isLowStock; }
    public void setIsLowStock(boolean isLowStock) { this.isLowStock = isLowStock; }
    public boolean getIsOutOfStock() { return isOutOfStock; }
    public void setIsOutOfStock(boolean isOutOfStock) { this.isOutOfStock = isOutOfStock; }
}
