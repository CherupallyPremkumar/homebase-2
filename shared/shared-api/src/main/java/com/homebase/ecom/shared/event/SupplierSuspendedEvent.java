package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a supplier is suspended by admin.
 * Consumed by: product BC to hide supplier's product listings.
 */
public class SupplierSuspendedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String EVENT_TYPE = "SUPPLIER_SUSPENDED";

    private String supplierId;
    private String reason;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public SupplierSuspendedEvent() {
    }

    public SupplierSuspendedEvent(String supplierId, String reason, LocalDateTime timestamp) {
        this.supplierId = supplierId;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
