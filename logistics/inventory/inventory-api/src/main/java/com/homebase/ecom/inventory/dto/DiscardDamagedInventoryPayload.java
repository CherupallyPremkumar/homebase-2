package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.util.List;

/**
 * Payload for the discardDamaged event.
 */
public class DiscardDamagedInventoryPayload extends MinimalPayload {

    /** Number of damaged units being discarded. If null, all damaged units are discarded. */
    private Integer discardQuantity;

    /** Reason for discarding. */
    private String reason;

    /** Specific unit identifiers being discarded (optional — if null, picks first N REPORTED). */
    private List<String> unitIdentifiers;

    public Integer getDiscardQuantity() { return discardQuantity; }
    public void setDiscardQuantity(Integer discardQuantity) { this.discardQuantity = discardQuantity; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public List<String> getUnitIdentifiers() { return unitIdentifiers; }
    public void setUnitIdentifiers(List<String> unitIdentifiers) { this.unitIdentifiers = unitIdentifiers; }
}
