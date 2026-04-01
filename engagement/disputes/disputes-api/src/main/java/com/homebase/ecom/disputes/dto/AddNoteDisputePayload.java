package com.homebase.ecom.disputes.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the addNote event.
 * Adds an investigation or communication note to the dispute.
 */
public class AddNoteDisputePayload extends MinimalPayload {

    private String note;

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
