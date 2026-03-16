package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.util.List;

/**
 * Payload for the returnDamaged event — damage discovered at warehouse.
 */
public class ReturnDamagedInventoryPayload extends MinimalPayload {

    /** Number of damaged units found at warehouse. */
    private Integer damagedQuantity;

    /** Individual damaged unit details for unit-level traceability. */
    private List<DamageUnit> damagedUnits;

    public Integer getDamagedQuantity() { return damagedQuantity; }
    public void setDamagedQuantity(Integer damagedQuantity) { this.damagedQuantity = damagedQuantity; }

    public List<DamageUnit> getDamagedUnits() { return damagedUnits; }
    public void setDamagedUnits(List<DamageUnit> damagedUnits) { this.damagedUnits = damagedUnits; }
}
