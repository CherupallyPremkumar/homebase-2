package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the retryDelivery event: DELIVERY_FAILED -> CHECK_DELIVERY_ATTEMPTS.
 * Warehouse schedules another delivery attempt.
 */
public class RetryDeliveryShippingPayload extends MinimalPayload {

    private String newDeliveryInstructions;

    public String getNewDeliveryInstructions() { return newDeliveryInstructions; }
    public void setNewDeliveryInstructions(String newDeliveryInstructions) { this.newDeliveryInstructions = newDeliveryInstructions; }
}
