package com.homebase.ecom.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the disableProduct event.
 * Temporarily removes product from catalog. Requires a reason.
 */
public class DisableProductProductPayload extends MinimalPayload {

    @NotBlank(message = "Disable reason is required")
    @Size(max = 1000, message = "Disable reason must not exceed 1000 characters")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
