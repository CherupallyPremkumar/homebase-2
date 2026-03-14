package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.util.List;

/**
 * Customized Payload for the refundComplete event.
 */
public class RefundCompleteOrderPayload extends MinimalPayload {
    private String refundTransactionId;
    private List<String> refundedItemIds;

    public String getRefundTransactionId() {
        return refundTransactionId;
    }

    public void setRefundTransactionId(String refundTransactionId) {
        this.refundTransactionId = refundTransactionId;
    }

    public List<String> getRefundedItemIds() {
        return refundedItemIds;
    }

    public void setRefundedItemIds(List<String> refundedItemIds) {
        this.refundedItemIds = refundedItemIds;
    }
}
