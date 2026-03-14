package com.homebase.ecom.checkout.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * Payment Method DTO
 */
public class PaymentMethodDTO {

    @NotBlank(message = "Payment provider is required")
    private String provider; // e.g., STRIPE, RAZORPAY

    @NotBlank(message = "Payment type is required")
    private String type; // e.g., CARD, UPI, WALLET

    private String token;
    private String paymentMethodId;

    private Map<String, String> metadata;

    // Getters and Setters
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
