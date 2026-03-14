package com.homebase.ecom.support.dto;

import org.chenile.workflow.param.MinimalPayload;

public class ReopenTicketPayload extends MinimalPayload {
    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
