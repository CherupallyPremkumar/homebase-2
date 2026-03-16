package com.homebase.ecom.onboarding.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the submit event — seller submits initial application.
 */
public class SubmitApplicationPayload extends MinimalPayload {
    private String applicantName;
    private String businessName;
    private String businessType;

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
}
