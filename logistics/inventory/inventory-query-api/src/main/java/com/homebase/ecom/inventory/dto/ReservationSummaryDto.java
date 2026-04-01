package com.homebase.ecom.inventory.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReservationSummaryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String inventoryItemId;
    private String sku;
    private String productId;
    private String orderId;
    private int quantity;
    private Timestamp reservedAt;
    private Timestamp expiresAt;
    private String status;

    public String getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Timestamp getReservedAt() { return reservedAt; }
    public void setReservedAt(Timestamp reservedAt) { this.reservedAt = reservedAt; }
    public Timestamp getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Timestamp expiresAt) { this.expiresAt = expiresAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
