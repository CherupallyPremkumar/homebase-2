package com.homebase.ecom.onboarding.dto;

import org.chenile.workflow.param.MinimalPayload;

public class RejectOnboardingPayload extends MinimalPayload {
    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
