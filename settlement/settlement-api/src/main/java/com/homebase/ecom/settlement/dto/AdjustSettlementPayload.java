package com.homebase.ecom.settlement.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.math.BigDecimal;

/**
 * Payload for the adjust event (dispute adjustment).
 */
public class AdjustSettlementPayload extends MinimalPayload {

    private BigDecimal adjustmentAmount;
    private String adjustmentReason;

    public BigDecimal getAdjustmentAmount() { return adjustmentAmount; }
    public void setAdjustmentAmount(BigDecimal adjustmentAmount) { this.adjustmentAmount = adjustmentAmount; }

    public String getAdjustmentReason() { return adjustmentReason; }
    public void setAdjustmentReason(String adjustmentReason) { this.adjustmentReason = adjustmentReason; }
}
