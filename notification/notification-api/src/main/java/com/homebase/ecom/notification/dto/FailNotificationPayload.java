package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the fail event in the notification STM (CREATED -> FAILED).
 */
public class FailNotificationPayload extends MinimalPayload {
    private String errorMessage;

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
