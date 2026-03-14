package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
    Customized Payload for the returnDamaged event.
*/
public class ReturnDamagedInventoryPayload extends MinimalPayload{

    /** Number of damaged units found at warehouse. */
    private Integer damagedQuantity;

    public Integer getDamagedQuantity() { return damagedQuantity; }
    public void setDamagedQuantity(Integer damagedQuantity) { this.damagedQuantity = damagedQuantity; }
}
