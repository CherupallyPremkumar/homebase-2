package com.homebase.ecom.compliance.query.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AgreementQueryDto {
    private String id;
    private String title;
    private String agreementType;
    private String versionLabel;
    private String stateId;
    private String flowId;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private boolean mandatoryAcceptance;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private String tenant;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAgreementType() { return agreementType; }
    public void setAgreementType(String agreementType) { this.agreementType = agreementType; }
    public String getVersionLabel() { return versionLabel; }
    public void setVersionLabel(String versionLabel) { this.versionLabel = versionLabel; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public boolean isMandatoryAcceptance() { return mandatoryAcceptance; }
    public void setMandatoryAcceptance(boolean mandatoryAcceptance) { this.mandatoryAcceptance = mandatoryAcceptance; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(LocalDateTime lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
