package com.homebase.ecom.user.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for KYC submission.
 */
public class SubmitKycUserPayload extends MinimalPayload {
    private String documentType;  // AADHAAR, PAN, PASSPORT, etc.
    private String documentId;

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
}
