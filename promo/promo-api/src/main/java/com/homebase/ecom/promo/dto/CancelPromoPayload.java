package com.homebase.ecom.promo.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for cancelling a promo. Requires a reason.
 */
public class CancelPromoPayload extends MinimalPayload {
    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
