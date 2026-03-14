package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the damageFound event.
 * Contains the count of damaged units found during warehouse inspection.
 */
public class DamageFoundInventoryPayload extends MinimalPayload {

    /**
     * Number of units found to be damaged.
     * Used to evaluate policies.damage.autoDiscardOnSevereDamagePercent.
     */
    private Integer damagedQuantity;

    public Integer getDamagedQuantity() {
        return damagedQuantity;
    }

    public void setDamagedQuantity(Integer damagedQuantity) {
        this.damagedQuantity = damagedQuantity;
    }
}
