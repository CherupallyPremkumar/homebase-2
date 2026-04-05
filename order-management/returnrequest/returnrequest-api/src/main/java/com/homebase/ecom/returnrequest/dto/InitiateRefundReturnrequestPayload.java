package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.math.BigDecimal;

/**
 * Payload for the initiateRefund event (INSPECTED -> REFUND_INITIATED).
 * System or Finance triggers the refund after inspection.
 */
public class InitiateRefundReturnrequestPayload extends MinimalPayload {

    private BigDecimal finalRefundAmount;
    private String refundMethod; // ORIGINAL_PAYMENT, WALLET, STORE_CREDIT

    public BigDecimal getFinalRefundAmount() { return finalRefundAmount; }
    public void setFinalRefundAmount(BigDecimal finalRefundAmount) { this.finalRefundAmount = finalRefundAmount; }

    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
}
