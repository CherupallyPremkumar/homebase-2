package com.homebase.ecom.payment.dto;

/**
 * Payload for recording refund failure at gateway.
 */
public class FailRefundPayload {
    private String comment;
    private String failureReason;
    private String gatewayResponse;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public String getGatewayResponse() { return gatewayResponse; }
    public void setGatewayResponse(String gatewayResponse) { this.gatewayResponse = gatewayResponse; }
}
