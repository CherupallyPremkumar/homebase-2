package com.homebase.ecom.notification.domain.model;

/**
 * User preferences for notification delivery.
 */
public class NotificationPreference {
    private String id;
    private String userId;
    private String channel;
    private String category;
    private boolean enabled;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
