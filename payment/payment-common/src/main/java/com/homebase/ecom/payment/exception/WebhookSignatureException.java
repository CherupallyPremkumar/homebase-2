package com.homebase.ecom.payment.exception;

/**
 * Exception thrown when webhook signature verification fails.
 * This indicates a potential security issue - either an attacker
 * attempting to send fraudulent webhooks, or a misconfigured secret.
 */
public class WebhookSignatureException extends RuntimeException {

    private final String gatewayType;

    public WebhookSignatureException(String gatewayType, String message) {
        super(message);
        this.gatewayType = gatewayType;
    }

    public WebhookSignatureException(String gatewayType, String message, Throwable cause) {
        super(message, cause);
        this.gatewayType = gatewayType;
    }

    public String getGatewayType() {
        return gatewayType;
    }
}
