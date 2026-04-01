package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Events published to onboarding.events topic.
 */
public class OnboardingEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ONBOARDING_STARTED = "ONBOARDING_STARTED";
    public static final String ONBOARDING_COMPLETED = "ONBOARDING_COMPLETED";
    public static final String ONBOARDING_REJECTED = "ONBOARDING_REJECTED";

    private String onboardingId;
    private String supplierId;
    private String businessName;
    private String eventType;
    private String reason;
    private LocalDateTime timestamp;

    public OnboardingEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public OnboardingEvent(String onboardingId, String eventType) {
        this.onboardingId = onboardingId;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }

    public String getOnboardingId() { return onboardingId; }
    public void setOnboardingId(String onboardingId) { this.onboardingId = onboardingId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
