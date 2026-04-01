package com.homebase.ecom.user.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for admin reinstatement of suspended user.
 */
public class ReinstateUserPayload extends MinimalPayload {
    private String reinstatedBy;
    private String notes;

    public String getReinstatedBy() { return reinstatedBy; }
    public void setReinstatedBy(String reinstatedBy) { this.reinstatedBy = reinstatedBy; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
