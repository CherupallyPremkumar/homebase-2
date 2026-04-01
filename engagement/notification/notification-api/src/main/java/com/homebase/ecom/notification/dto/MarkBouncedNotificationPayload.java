package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for marking a notification as bounced (permanently failed).
 */
public class MarkBouncedNotificationPayload extends MinimalPayload {
    private String bounceReason;

    public String getBounceReason() { return bounceReason; }
    public void setBounceReason(String bounceReason) { this.bounceReason = bounceReason; }
}
