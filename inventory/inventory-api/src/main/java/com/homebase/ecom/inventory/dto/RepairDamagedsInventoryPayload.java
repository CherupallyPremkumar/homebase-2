package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
    Customized Payload for the repairDamageds event.
*/
public class RepairDamagedsInventoryPayload extends MinimalPayload{

    /** Number of damaged units that were successfully repaired. */
    private Integer repairedQuantity;

    public Integer getRepairedQuantity() { return repairedQuantity; }
    public void setRepairedQuantity(Integer repairedQuantity) { this.repairedQuantity = repairedQuantity; }
}
