package com.homebase.ecom.user.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the deleteAccount event (transitions to DELETED from multiple states).
 */
public class DeleteAccountPayload extends MinimalPayload {
    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
