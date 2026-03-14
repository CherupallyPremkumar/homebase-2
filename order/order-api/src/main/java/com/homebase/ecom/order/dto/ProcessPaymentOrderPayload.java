package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the processPayment event.
 */
public class ProcessPaymentOrderPayload extends MinimalPayload {
    private String paymentId;
    private String errorDetails;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
