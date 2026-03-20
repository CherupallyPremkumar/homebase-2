package com.homebase.ecom.returnprocessing.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the rejectItem event in the return processing saga.
 * Carries rejection reason from warehouse inspection.
 */
public class RejectItemPayload extends MinimalPayload {

    private String rejectionReason;
    private String damageDescription;

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }
}
