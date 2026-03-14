package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the confirmDelivery event.
 */
public class ConfirmDeliveryOrderPayload extends MinimalPayload {
    private String confirmedBy;

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }
}
