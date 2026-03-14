package com.homebase.ecom.product.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the rejectProduct event.
 * Includes a mandatory audit comment explaining the reason for rejection.
 */
public class RejectProductProductPayload extends MinimalPayload {

    /**
     * Reason for rejection. Required when
     * policies.lifecycle.rejectionRequiresComment = true.
     * Examples: "Poor image quality", "Incorrect category", "Misleading
     * description".
     */
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
