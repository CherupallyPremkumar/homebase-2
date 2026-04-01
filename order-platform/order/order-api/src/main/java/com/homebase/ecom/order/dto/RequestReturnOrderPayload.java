package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for requestReturn event -- customer requests a return.
 */
public class RequestReturnOrderPayload extends MinimalPayload {
    private String reason;
    private String itemId;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
}
