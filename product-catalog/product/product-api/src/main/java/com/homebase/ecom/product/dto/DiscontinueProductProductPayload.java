package com.homebase.ecom.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the discontinueProduct event.
 * Permanently end-of-life the product. Requires a reason.
 */
public class DiscontinueProductProductPayload extends MinimalPayload {

    @NotBlank(message = "Discontinue reason is required")
    @Size(max = 1000, message = "Discontinue reason must not exceed 1000 characters")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
