package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.util.List;

/**
 * Payload for the repairDamaged event (warehouse-level repair).
 */
public class RepairDamagedInventoryPayload extends MinimalPayload {

    /** Number of damaged units that were successfully repaired. */
    private Integer repairedQuantity;

    /** Specific unit identifiers being repaired (optional — if null, picks first N REPORTED). */
    private List<String> unitIdentifiers;

    public Integer getRepairedQuantity() { return repairedQuantity; }
    public void setRepairedQuantity(Integer repairedQuantity) { this.repairedQuantity = repairedQuantity; }

    public List<String> getUnitIdentifiers() { return unitIdentifiers; }
    public void setUnitIdentifiers(List<String> unitIdentifiers) { this.unitIdentifiers = unitIdentifiers; }
}
