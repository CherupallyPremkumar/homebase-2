package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the returnShipment event: DELIVERY_FAILED -> RETURNED.
 * Warehouse decides to return shipment instead of retrying.
 */
public class ReturnShipmentShippingPayload extends MinimalPayload {

    private String returnReason;

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
}
