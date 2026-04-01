package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the rejectSupplier event.
 * Admin rejects a supplier application with a mandatory reason.
 */
public class RejectSupplierPayload extends MinimalPayload {

    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
