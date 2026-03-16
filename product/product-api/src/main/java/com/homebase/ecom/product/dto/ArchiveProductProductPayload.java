package com.homebase.ecom.product.dto;

import jakarta.validation.constraints.Size;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the archiveProduct event.
 * Hides the product from catalog but preserves data for order history/returns.
 * Reason is optional — defaults to "Product archived".
 */
public class ArchiveProductProductPayload extends MinimalPayload {

    @Size(max = 1000, message = "Archive reason must not exceed 1000 characters")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
