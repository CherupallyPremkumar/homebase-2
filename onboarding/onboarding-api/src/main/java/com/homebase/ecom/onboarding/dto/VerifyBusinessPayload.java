package com.homebase.ecom.onboarding.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for verifyBusiness event — admin/compliance verifies business details.
 */
public class VerifyBusinessPayload extends MinimalPayload {
    private String verificationNotes;

    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) { this.verificationNotes = verificationNotes; }
}
