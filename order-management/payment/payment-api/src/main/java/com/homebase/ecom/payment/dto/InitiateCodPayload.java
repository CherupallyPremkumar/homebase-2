package com.homebase.ecom.payment.dto;

/**
 * Payload for initiateCod event: INITIATED → COD_PENDING.
 * Marks this payment as Cash on Delivery — no gateway involved.
 */
public class InitiateCodPayload {
    private String comment;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
