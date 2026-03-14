package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
    Customized Payload for the discardDamaged event.
*/
public class DiscardDamagedInventoryPayload extends MinimalPayload{

    /** Number of damaged units being discarded. If null, all damaged units are discarded. */
    private Integer discardQuantity;
    /** Reason for discarding. */
    private String reason;

    public Integer getDiscardQuantity() { return discardQuantity; }
    public void setDiscardQuantity(Integer discardQuantity) { this.discardQuantity = discardQuantity; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
