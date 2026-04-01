package com.homebase.ecom.review.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for requesting edits from the reviewer (UNDER_MODERATION -> EDIT_REQUESTED).
 */
public class RequestEditPayload extends MinimalPayload {
    private String editInstructions;

    public String getEditInstructions() { return editInstructions; }
    public void setEditInstructions(String editInstructions) { this.editInstructions = editInstructions; }
}
