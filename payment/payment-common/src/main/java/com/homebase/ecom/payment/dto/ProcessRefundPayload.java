package com.homebase.ecom.payment.dto;

public class ProcessRefundPayload {
    private String comment;
    private String gatewayTransactionId;
    private String gatewayResponse;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }
    public String getGatewayResponse() { return gatewayResponse; }
    public void setGatewayResponse(String gatewayResponse) { this.gatewayResponse = gatewayResponse; }
}
