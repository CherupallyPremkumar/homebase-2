package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the failDelivery event: OUT_FOR_DELIVERY -> DELIVERY_FAILED.
 * Contains failure reason from carrier.
 */
public class FailDeliveryShippingPayload extends MinimalPayload {

    private String failureReason;

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
