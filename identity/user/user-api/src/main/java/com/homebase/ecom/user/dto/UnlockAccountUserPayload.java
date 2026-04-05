package com.homebase.ecom.user.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for account unlock.
 */
public class UnlockAccountUserPayload extends MinimalPayload {
    private String unlockedBy; // "ADMIN" or "TIMEOUT"

    public String getUnlockedBy() { return unlockedBy; }
    public void setUnlockedBy(String unlockedBy) { this.unlockedBy = unlockedBy; }
}
