package com.homebase.ecom.disputes.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.math.BigDecimal;

/**
 * Payload for the resolveDispute event.
 * Carries the resolution outcome, notes, and optional refund amount.
 */
public class ResolveDisputePayload extends MinimalPayload {

    private String resolutionOutcome; // REFUND_CUSTOMER, SIDE_WITH_SELLER, PARTIAL_REFUND, SPLIT_DECISION
    private String resolutionNotes;
    private BigDecimal refundAmount;

    public String getResolutionOutcome() { return resolutionOutcome; }
    public void setResolutionOutcome(String resolutionOutcome) { this.resolutionOutcome = resolutionOutcome; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
}
