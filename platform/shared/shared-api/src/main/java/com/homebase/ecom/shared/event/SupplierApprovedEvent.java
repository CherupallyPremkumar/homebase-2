package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a supplier is approved.
 * Consumed by: onboarding BC to trigger training phase.
 */
public class SupplierApprovedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String EVENT_TYPE = "SUPPLIER_APPROVED";

    private String supplierId;
    private String businessName;
    private String onboardingId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public SupplierApprovedEvent() {
    }

    public SupplierApprovedEvent(String supplierId, String businessName, LocalDateTime timestamp) {
        this.supplierId = supplierId;
        this.businessName = businessName;
        this.timestamp = timestamp;
    }

    public SupplierApprovedEvent(String supplierId, String businessName, String onboardingId, LocalDateTime timestamp) {
        this.supplierId = supplierId;
        this.businessName = businessName;
        this.onboardingId = onboardingId;
        this.timestamp = timestamp;
    }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getOnboardingId() { return onboardingId; }
    public void setOnboardingId(String onboardingId) { this.onboardingId = onboardingId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
