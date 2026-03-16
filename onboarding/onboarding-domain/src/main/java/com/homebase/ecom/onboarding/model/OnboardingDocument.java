package com.homebase.ecom.onboarding.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Value object representing a document submitted during onboarding.
 */
public class OnboardingDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;         // TAX_ID, BUSINESS_LICENSE, BANK_PROOF
    private String fileUrl;
    private String status;       // PENDING, VERIFIED, REJECTED
    private String rejectionReason;
    private LocalDateTime verifiedAt;

    public OnboardingDocument() {
        this.status = "PENDING";
    }

    public OnboardingDocument(String type, String fileUrl) {
        this.type = type;
        this.fileUrl = fileUrl;
        this.status = "PENDING";
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }

    public boolean isVerified() { return "VERIFIED".equals(status); }
    public boolean isRejected() { return "REJECTED".equals(status); }
    public boolean isPending() { return "PENDING".equals(status); }
}
