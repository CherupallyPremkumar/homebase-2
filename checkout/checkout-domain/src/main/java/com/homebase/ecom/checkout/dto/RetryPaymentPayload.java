package com.homebase.ecom.checkout.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the retryPayment event.
 * Customer retries payment after a failure (max 3 retries enforced by CHECK_RETRY_ALLOWED auto-state).
 */
public class RetryPaymentPayload extends MinimalPayload {
    public String paymentMethodId;
}
