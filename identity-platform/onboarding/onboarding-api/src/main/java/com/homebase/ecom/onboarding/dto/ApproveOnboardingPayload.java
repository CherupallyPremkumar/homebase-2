package com.homebase.ecom.onboarding.dto;

import org.chenile.workflow.param.MinimalPayload;

public class ApproveOnboardingPayload extends MinimalPayload {
    private Double commissionOverride;

    public Double getCommissionOverride() { return commissionOverride; }
    public void setCommissionOverride(Double commissionOverride) { this.commissionOverride = commissionOverride; }
}
