package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for paymentFailed event — triggered by payment.events PAYMENT_FAILED.
 */
public class PaymentFailedOrderPayload extends MinimalPayload {
    private String errorCode;
    private String errorDetails;

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getErrorDetails() { return errorDetails; }
    public void setErrorDetails(String errorDetails) { this.errorDetails = errorDetails; }
}
