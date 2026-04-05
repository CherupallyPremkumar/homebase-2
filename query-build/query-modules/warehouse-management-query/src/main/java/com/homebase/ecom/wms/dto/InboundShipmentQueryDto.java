package com.homebase.ecom.wms.dto;

import java.io.Serializable;
import java.util.Date;

public class InboundShipmentQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String inventoryItemId;
    private String productId;
    private String variantId;
    private String sku;
    private int quantity;
    private String movementType;
    private String referenceId;
    private String fulfillmentCenterId;
    private Date movementTime;
    private String reason;
    private String supplierId;
    private String productName;
    private String brand;
    private String fulfillmentCenterName;
    private String fulfillmentCenterCode;
    private String supplierName;
    private String supplierState;

    public String getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public String getFulfillmentCenterId() { return fulfillmentCenterId; }
    public void setFulfillmentCenterId(String fulfillmentCenterId) { this.fulfillmentCenterId = fulfillmentCenterId; }
    public Date getMovementTime() { return movementTime; }
    public void setMovementTime(Date movementTime) { this.movementTime = movementTime; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getFulfillmentCenterName() { return fulfillmentCenterName; }
    public void setFulfillmentCenterName(String fulfillmentCenterName) { this.fulfillmentCenterName = fulfillmentCenterName; }
    public String getFulfillmentCenterCode() { return fulfillmentCenterCode; }
    public void setFulfillmentCenterCode(String fulfillmentCenterCode) { this.fulfillmentCenterCode = fulfillmentCenterCode; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getSupplierState() { return supplierState; }
    public void setSupplierState(String supplierState) { this.supplierState = supplierState; }
}
