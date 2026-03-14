package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the cancelOrder event.
 * Enriched with 'reason' field for cancellation reason policy enforcement.
 * Controlled by: order.json → policies.cancellation.requireCancellationReason
 */
public class CancelOrderOrderPayload extends MinimalPayload {

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
