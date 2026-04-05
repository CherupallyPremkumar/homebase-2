package com.homebase.ecom.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the recallProduct event.
 * Requires a mandatory recall reason (safety/compliance) and recall reference number.
 */
public class RecallProductProductPayload extends MinimalPayload {

    @NotBlank(message = "Recall reason is required")
    @Size(min = 10, max = 2000, message = "Recall reason must be between 10 and 2000 characters")
    private String recallReason;

    @NotBlank(message = "Recall reference number is required")
    @Size(max = 100, message = "Recall reference number must not exceed 100 characters")
    private String recallReferenceNumber;

    public String getRecallReason() {
        return recallReason;
    }

    public void setRecallReason(String recallReason) {
        this.recallReason = recallReason;
    }

    public String getRecallReferenceNumber() {
        return recallReferenceNumber;
    }

    public void setRecallReferenceNumber(String recallReferenceNumber) {
        this.recallReferenceNumber = recallReferenceNumber;
    }
}
