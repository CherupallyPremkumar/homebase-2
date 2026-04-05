package com.homebase.ecom.user.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for account lock (system-triggered).
 */
public class LockAccountUserPayload extends MinimalPayload {
    private String reason;
    private int failedAttempts;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }
}
