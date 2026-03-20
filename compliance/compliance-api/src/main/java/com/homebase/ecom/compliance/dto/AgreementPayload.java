package com.homebase.ecom.compliance.dto;

import java.time.LocalDate;
import org.chenile.workflow.param.MinimalPayload;

public class AgreementPayload extends MinimalPayload {
    private String title;
    private String agreementType;
    private String versionLabel;
    private String contentUrl;
    private String contentHash;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private boolean mandatoryAcceptance;
    private String supersededById;
    private String reason;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAgreementType() { return agreementType; }
    public void setAgreementType(String agreementType) { this.agreementType = agreementType; }
    public String getVersionLabel() { return versionLabel; }
    public void setVersionLabel(String versionLabel) { this.versionLabel = versionLabel; }
    public String getContentUrl() { return contentUrl; }
    public void setContentUrl(String contentUrl) { this.contentUrl = contentUrl; }
    public String getContentHash() { return contentHash; }
    public void setContentHash(String contentHash) { this.contentHash = contentHash; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public boolean isMandatoryAcceptance() { return mandatoryAcceptance; }
    public void setMandatoryAcceptance(boolean mandatoryAcceptance) { this.mandatoryAcceptance = mandatoryAcceptance; }
    public String getSupersededById() { return supersededById; }
    public void setSupersededById(String supersededById) { this.supersededById = supersededById; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
