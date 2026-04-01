package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a supplier transitions to ACTIVE state.
 * Consumed by: product BC to enable supplier's product listings.
 */
public class SupplierActivatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String EVENT_TYPE = "SUPPLIER_ACTIVATED";

    private String supplierId;
    private String supplierName;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public SupplierActivatedEvent() {
    }

    public SupplierActivatedEvent(String supplierId, String supplierName, LocalDateTime timestamp) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.timestamp = timestamp;
    }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
