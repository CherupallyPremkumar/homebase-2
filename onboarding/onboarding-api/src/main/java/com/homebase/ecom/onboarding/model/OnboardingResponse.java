package com.homebase.ecom.onboarding.model;

public class OnboardingResponse {
    private String onboardingId;
    private String supplierId;
    private String status;
    private String message;

    public OnboardingResponse() {}

    public String getOnboardingId() {
        return onboardingId;
    }

    public void setOnboardingId(String onboardingId) {
        this.onboardingId = onboardingId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final OnboardingResponse response = new OnboardingResponse();

        public Builder onboardingId(String onboardingId) {
            response.setOnboardingId(onboardingId);
            return this;
        }

        public Builder supplierId(String supplierId) {
            response.setSupplierId(supplierId);
            return this;
        }

        public Builder status(String status) {
            response.setStatus(status);
            return this;
        }

        public Builder message(String message) {
            response.setMessage(message);
            return this;
        }

        public OnboardingResponse build() {
            return response;
        }
    }
}
