package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the outForDelivery event.
 * Contains delivery agent and hub information.
 */
public class OutForDeliveryShippingPayload extends MinimalPayload {

    private String deliveryAgent;
    private String localHub;

    public String getDeliveryAgent() { return deliveryAgent; }
    public void setDeliveryAgent(String deliveryAgent) { this.deliveryAgent = deliveryAgent; }

    public String getLocalHub() { return localHub; }
    public void setLocalHub(String localHub) { this.localHub = localHub; }
}
