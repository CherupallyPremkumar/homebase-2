package com.homebase.ecom.returnprocessing.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the inspectItem event in the return processing saga.
 * Carries item condition info from warehouse inspection.
 */
public class InspectItemPayload extends MinimalPayload {

    private String itemCondition;
    private String damageDescription;
    private boolean resellable;

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public boolean isResellable() {
        return resellable;
    }

    public void setResellable(boolean resellable) {
        this.resellable = resellable;
    }
}
