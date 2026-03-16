package com.homebase.ecom.onboarding.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for verifyDocuments event — admin/compliance verifies submitted documents.
 */
public class VerifyDocumentsPayload extends MinimalPayload {
    private String verificationNotes;

    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) { this.verificationNotes = verificationNotes; }
}
