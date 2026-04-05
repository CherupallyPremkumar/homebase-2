package com.homebase.ecom.checkout.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for paymentSuccess / paymentFailed events.
 * Comes from payment webhook or Kafka event.
 */
public class PaymentResultPayload extends MinimalPayload {
    public String paymentId;
    public String transactionId;
    public String failureReason;
}
