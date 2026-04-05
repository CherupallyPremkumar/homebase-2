package com.homebase.ecom.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the resolveRecall event.
 * Admin resolves the recall — product moves to DISCONTINUED.
 */
public class ResolveRecallProductPayload extends MinimalPayload {

    @NotBlank(message = "Resolution summary is required")
    @Size(min = 10, max = 2000, message = "Resolution summary must be between 10 and 2000 characters")
    private String resolutionSummary;

    public String getResolutionSummary() {
        return resolutionSummary;
    }

    public void setResolutionSummary(String resolutionSummary) {
        this.resolutionSummary = resolutionSummary;
    }
}
