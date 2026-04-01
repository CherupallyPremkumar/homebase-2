package com.homebase.ecom.user.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for admin KYC verification.
 */
public class VerifyKycUserPayload extends MinimalPayload {
    private boolean approved;
    private String verificationNotes;

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) { this.verificationNotes = verificationNotes; }
}
