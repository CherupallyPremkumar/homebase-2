package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the completeReturn event (REFUND_INITIATED -> COMPLETED).
 * Triggered by the return-processing saga when refund is processed.
 */
public class CompleteReturnPayload extends MinimalPayload {

    private String refundTransactionId;

    public String getRefundTransactionId() { return refundTransactionId; }
    public void setRefundTransactionId(String refundTransactionId) { this.refundTransactionId = refundTransactionId; }
}
