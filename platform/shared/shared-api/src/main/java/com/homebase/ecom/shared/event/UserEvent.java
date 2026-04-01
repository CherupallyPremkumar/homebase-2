package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Cross-service event published to user.events Kafka topic.
 * Consumed by notification, order, and other services that need user state changes.
 */
public class UserEvent implements Serializable {

    public static final String USER_REGISTERED = "USER_REGISTERED";
    public static final String USER_VERIFIED = "USER_VERIFIED";
    public static final String USER_SUSPENDED = "USER_SUSPENDED";
    public static final String USER_DEACTIVATED = "USER_DEACTIVATED";
    public static final String USER_KYC_VERIFIED = "USER_KYC_VERIFIED";

    private String userId;
    private String email;
    private String eventType;
    private String role;
    private LocalDateTime timestamp;

    public UserEvent() {}

    public UserEvent(String userId, String email, String eventType) {
        this.userId = userId;
        this.email = email;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }

    public UserEvent(String userId, String email, String eventType, String role) {
        this(userId, email, eventType);
        this.role = role;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
