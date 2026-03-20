package com.homebase.ecom.payment.dto;

/**
 * Payload for recording payment timeout.
 */
public class TimeoutPaymentPayload {
    private String comment;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
