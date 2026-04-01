package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when product stock levels change.
 */
public class ProductStockUpdatedEvent implements Serializable {
    
    public enum StockStatus { IN_STOCK, OUT_OF_STOCK }

    private String productId;
    private int availableQuantity;
    private StockStatus status;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ProductStockUpdatedEvent() {}

    public ProductStockUpdatedEvent(String productId, int availableQuantity, StockStatus status) {
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public StockStatus getStatus() {
        return status;
    }

    public void setStatus(StockStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
