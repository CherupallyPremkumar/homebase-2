package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event published when a product is recalled for safety/compliance.
 * Triggers cross-service actions:
 * - Catalog: remove from storefront immediately
 * - Order: block new orders for this product
 * - Fulfillment: halt in-progress shipments
 * - Inventory: quarantine remaining stock
 * - Notification: alert customers with active orders
 */
public class ProductRecalledEvent implements Serializable {

    public static final String EVENT_TYPE = "PRODUCT_RECALLED";

    private String productId;
    private String recallReason;
    private String recallReferenceNumber;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public ProductRecalledEvent() {
    }

    public ProductRecalledEvent(String productId, String recallReason, String recallReferenceNumber) {
        this.productId = productId;
        this.recallReason = recallReason;
        this.recallReferenceNumber = recallReferenceNumber;
        this.timestamp = LocalDateTime.now();
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getRecallReason() { return recallReason; }
    public void setRecallReason(String recallReason) { this.recallReason = recallReason; }
    public String getRecallReferenceNumber() { return recallReferenceNumber; }
    public void setRecallReferenceNumber(String recallReferenceNumber) { this.recallReferenceNumber = recallReferenceNumber; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
