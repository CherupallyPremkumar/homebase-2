package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the returnRequested event.
 * Contains reason for return.
 */
public class ReturnRequestedShippingPayload extends MinimalPayload {

    private String returnReason;

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
}
