package com.homebase.ecom.checkout.domain.model;

public class PaymentDetails {
    private final String paymentId;
    private final String paymentUrl;
    private final String status;

    public PaymentDetails(String paymentId, String paymentUrl, String status) {
        this.paymentId = paymentId;
        this.paymentUrl = paymentUrl;
        this.status = status;
    }

    public String getPaymentId() { return paymentId; }
    public String getPaymentUrl() { return paymentUrl; }
    public String getStatus() { return status; }
}
