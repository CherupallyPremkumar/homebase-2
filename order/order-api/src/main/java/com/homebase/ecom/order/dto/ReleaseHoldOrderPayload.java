package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for releaseHold event -- admin releases order from hold.
 */
public class ReleaseHoldOrderPayload extends MinimalPayload {
    private String releaseNote;

    public String getReleaseNote() { return releaseNote; }
    public void setReleaseNote(String releaseNote) { this.releaseNote = releaseNote; }
}
