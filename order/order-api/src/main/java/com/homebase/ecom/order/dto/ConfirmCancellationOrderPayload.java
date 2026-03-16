package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for confirmCancellation event.
 */
public class ConfirmCancellationOrderPayload extends MinimalPayload {
    private String adminNote;

    public String getAdminNote() { return adminNote; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }
}
