package com.homebase.ecom.offer.service.cmds;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for reject event. Requires a reason.
 */
public class RejectOfferPayload extends MinimalPayload {
    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
