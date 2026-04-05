package com.homebase.ecom.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the rejectUpdate event.
 * Admin rejects the pending changes — product returns to PUBLISHED as-is.
 */
public class RejectUpdateProductPayload extends MinimalPayload {

    @NotBlank(message = "Rejection comment is required")
    @Size(min = 10, max = 1000, message = "Rejection comment must be between 10 and 1000 characters")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
