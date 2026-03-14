package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the markDelivered event.
 * Contains delivery proof and signature information.
 */
public class MarkDeliveredShippingPayload extends MinimalPayload {

    private String deliveryProof;
    private String receivedBy;

    public String getDeliveryProof() { return deliveryProof; }
    public void setDeliveryProof(String deliveryProof) { this.deliveryProof = deliveryProof; }

    public String getReceivedBy() { return receivedBy; }
    public void setReceivedBy(String receivedBy) { this.receivedBy = receivedBy; }
}
