package com.homebase.ecom.payment.dto;

import java.math.BigDecimal;

/**
 * Payload for completeRefund event: REFUND_PROCESSING → CHECK_REFUND_TYPE.
 * Carries the confirmed refund amount from gateway.
 */
public class CompleteRefundPayload {
    private String comment;
    private String gatewayTransactionId;
    private String gatewayResponse;
    private BigDecimal refundedAmount;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }
    public String getGatewayResponse() { return gatewayResponse; }
    public void setGatewayResponse(String gatewayResponse) { this.gatewayResponse = gatewayResponse; }
    public BigDecimal getRefundedAmount() { return refundedAmount; }
    public void setRefundedAmount(BigDecimal refundedAmount) { this.refundedAmount = refundedAmount; }
}
