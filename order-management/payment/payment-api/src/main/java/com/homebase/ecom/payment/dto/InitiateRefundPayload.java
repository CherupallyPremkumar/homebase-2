package com.homebase.ecom.payment.dto;

public class InitiateRefundPayload {
    private String comment;
    private String refundReason;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }
}
