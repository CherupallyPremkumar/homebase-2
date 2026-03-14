package com.homebase.ecom.checkout.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * Request DTO for checkout cancellation
 */
public class CheckoutCancellationRequest {

    @NotNull(message = "Checkout ID is required")
    private String checkoutId;

    @NotBlank(message = "Cancellation reason is required")
    private String reason;

    private Map<String, String> metadata;

    // Getters and Setters
    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
