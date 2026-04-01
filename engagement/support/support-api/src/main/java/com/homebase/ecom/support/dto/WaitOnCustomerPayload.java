package com.homebase.ecom.support.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for putting a ticket on hold while waiting for customer response
 * (IN_PROGRESS -> WAITING_ON_CUSTOMER).
 */
public class WaitOnCustomerPayload extends MinimalPayload {
    private String waitReason;

    public String getWaitReason() { return waitReason; }
    public void setWaitReason(String waitReason) { this.waitReason = waitReason; }
}
