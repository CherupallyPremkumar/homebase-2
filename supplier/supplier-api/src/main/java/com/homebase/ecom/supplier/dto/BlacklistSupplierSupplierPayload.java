package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the blacklistSupplier event.
 * Admin permanently bans a supplier with a mandatory reason.
 */
public class BlacklistSupplierSupplierPayload extends MinimalPayload {

    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
