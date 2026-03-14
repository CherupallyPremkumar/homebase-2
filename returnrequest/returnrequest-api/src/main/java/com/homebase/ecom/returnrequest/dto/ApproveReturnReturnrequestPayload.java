package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.math.BigDecimal;

/**
 * Payload for the approveReturn event.
 * Carries refund type and optional override amount.
 */
public class ApproveReturnReturnrequestPayload extends MinimalPayload {

    private String refundType; // FULL or PARTIAL
    private BigDecimal refundAmount; // optional override

    public String getRefundType() { return refundType; }
    public void setRefundType(String refundType) { this.refundType = refundType; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
}
