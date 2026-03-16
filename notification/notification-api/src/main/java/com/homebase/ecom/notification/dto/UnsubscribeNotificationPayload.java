package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the unsubscribe event (any state -> UNSUBSCRIBED).
 * Allows a CUSTOMER to opt out of notification channels.
 */
public class UnsubscribeNotificationPayload extends MinimalPayload {
    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
