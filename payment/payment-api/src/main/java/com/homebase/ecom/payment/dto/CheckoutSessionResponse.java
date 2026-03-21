package com.homebase.ecom.payment.dto;

public class CheckoutSessionResponse {
    private String sessionId;
    private String clientSecret;
    private String checkoutUrl;
    private String gatewayType;

    public CheckoutSessionResponse() {
    }

    public CheckoutSessionResponse(String sessionId, String clientSecret, String checkoutUrl, String gatewayType) {
        this.sessionId = sessionId;
        this.clientSecret = clientSecret;
        this.checkoutUrl = checkoutUrl;
        this.gatewayType = gatewayType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public static class Builder {
        private String sessionId;
        private String clientSecret;
        private String checkoutUrl;
        private String gatewayType;

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder checkoutUrl(String checkoutUrl) {
            this.checkoutUrl = checkoutUrl;
            return this;
        }

        public Builder gatewayType(String gatewayType) {
            this.gatewayType = gatewayType;
            return this;
        }

        public CheckoutSessionResponse build() {
            return new CheckoutSessionResponse(sessionId, clientSecret, checkoutUrl, gatewayType);
        }
    }
}
