package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event published when a product is archived.
 * Catalog module hides the product but data is preserved for order history/returns.
 */
public class ProductArchivedEvent implements Serializable {

    public static final String EVENT_TYPE = "PRODUCT_ARCHIVED";

    private String productId;
    private String reason;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public ProductArchivedEvent() {
    }

    public ProductArchivedEvent(String productId, String reason) {
        this.productId = productId;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
