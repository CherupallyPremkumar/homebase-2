package com.homebase.ecom.cart.dto;

import java.io.Serializable;

public class PaymentResultPayload implements Serializable {
    private static final long serialVersionUID = 1L;
    private String paymentIntentId;
    private String status;
    private String errorMessage;

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
