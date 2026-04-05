package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the terminateSupplier event.
 * Admin permanently removes a supplier from the platform.
 */
public class TerminateSupplierPayload extends MinimalPayload {

    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
