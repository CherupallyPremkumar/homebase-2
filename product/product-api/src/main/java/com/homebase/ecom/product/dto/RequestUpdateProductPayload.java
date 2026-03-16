package com.homebase.ecom.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the requestUpdate event.
 * Supplier submits proposed changes to a published product.
 * The proposed changes are stored as a JSON snapshot in pendingChanges.
 */
public class RequestUpdateProductPayload extends MinimalPayload {

    @NotBlank(message = "Pending changes JSON is required")
    @Size(max = 50000, message = "Pending changes must not exceed 50000 characters")
    private String pendingChanges;

    public String getPendingChanges() {
        return pendingChanges;
    }

    public void setPendingChanges(String pendingChanges) {
        this.pendingChanges = pendingChanges;
    }
}
