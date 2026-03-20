package com.homebase.ecom.payment.dto;

/**
 * Payload for initiating a chargeback dispute.
 */
public class InitiateChargebackPayload {
    private String comment;
    private String chargebackId;
    private String reason;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getChargebackId() { return chargebackId; }
    public void setChargebackId(String chargebackId) { this.chargebackId = chargebackId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
