package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a supplier is permanently blacklisted.
 * Consumed by: product BC to disable all supplier's products,
 *              settlement BC to hold any pending payouts.
 */
public class SupplierBlacklistedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String EVENT_TYPE = "SUPPLIER_BLACKLISTED";

    private String supplierId;
    private String reason;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public SupplierBlacklistedEvent() {
    }

    public SupplierBlacklistedEvent(String supplierId, String reason, LocalDateTime timestamp) {
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
