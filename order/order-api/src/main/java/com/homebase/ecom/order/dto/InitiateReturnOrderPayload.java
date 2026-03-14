package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.util.List;

/**
 * Customized Payload for the initiateReturn event.
 */
public class InitiateReturnOrderPayload extends MinimalPayload {
    private String reason;
    private List<String> itemIds;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }
}
