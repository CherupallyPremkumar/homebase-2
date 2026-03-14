package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the deliverOrder event.
 */
public class DeliverOrderOrderPayload extends MinimalPayload {
    private String deliveryProof;

    public String getDeliveryProof() {
        return deliveryProof;
    }

    public void setDeliveryProof(String deliveryProof) {
        this.deliveryProof = deliveryProof;
    }
}
