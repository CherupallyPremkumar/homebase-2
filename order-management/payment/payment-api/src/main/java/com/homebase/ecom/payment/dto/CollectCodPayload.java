package com.homebase.ecom.payment.dto;

import java.math.BigDecimal;

/**
 * Payload for confirming cash-on-delivery collection.
 */
public class CollectCodPayload {
    private String comment;
    private BigDecimal collectedAmount;
    private String collectedBy;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public BigDecimal getCollectedAmount() { return collectedAmount; }
    public void setCollectedAmount(BigDecimal collectedAmount) { this.collectedAmount = collectedAmount; }
    public String getCollectedBy() { return collectedBy; }
    public void setCollectedBy(String collectedBy) { this.collectedBy = collectedBy; }
}
