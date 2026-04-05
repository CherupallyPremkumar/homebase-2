package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the rejectStock event.
 * Includes a mandatory audit comment explaining the reason for rejection.
 */
public class RejectStockInventoryPayload extends MinimalPayload {

    /**
     * Reason for rejection. Required when rules.inspection.rejectionRequiresComment
     * = true.
     * Examples: "Poor quality stitching", "Wrong product sent", "Colour mismatch".
     */
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
