package com.homebase.ecom.settlement.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the dispute event.
 */
public class DisputeSettlementPayload extends MinimalPayload {

    private String disputeReason;

    public String getDisputeReason() { return disputeReason; }
    public void setDisputeReason(String disputeReason) { this.disputeReason = disputeReason; }
}
