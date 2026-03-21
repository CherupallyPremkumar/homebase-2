package com.homebase.ecom.payment.dto;

/**
 * Payload for resolving a chargeback dispute.
 * chargebackWon = true means merchant won, false means customer won.
 */
public class ResolveChargebackPayload {
    private String comment;
    private boolean chargebackWon;
    private String evidenceNotes;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public boolean isChargebackWon() { return chargebackWon; }
    public void setChargebackWon(boolean chargebackWon) { this.chargebackWon = chargebackWon; }
    public String getEvidenceNotes() { return evidenceNotes; }
    public void setEvidenceNotes(String evidenceNotes) { this.evidenceNotes = evidenceNotes; }
}
