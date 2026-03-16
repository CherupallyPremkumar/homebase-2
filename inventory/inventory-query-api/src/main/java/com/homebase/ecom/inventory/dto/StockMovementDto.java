package com.homebase.ecom.inventory.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class StockMovementDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String inventoryItemId;
    private String sku;
    private String productId;
    private String type;
    private int quantity;
    private String referenceId;
    private String fulfillmentCenterId;
    private Timestamp movementTime;
    private String reason;

    public String getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public String getFulfillmentCenterId() { return fulfillmentCenterId; }
    public void setFulfillmentCenterId(String fulfillmentCenterId) { this.fulfillmentCenterId = fulfillmentCenterId; }
    public Timestamp getMovementTime() { return movementTime; }
    public void setMovementTime(Timestamp movementTime) { this.movementTime = movementTime; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
