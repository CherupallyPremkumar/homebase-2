package com.homebase.ecom.compliance.dto;

import java.time.LocalDate;
import org.chenile.workflow.param.MinimalPayload;

public class PlatformPolicyPayload extends MinimalPayload {
    private String title;
    private String policyCategory;
    private String versionLabel;
    private String contentUrl;
    private String contentHash;
    private String summaryText;
    private LocalDate effectiveDate;
    private String reason;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPolicyCategory() { return policyCategory; }
    public void setPolicyCategory(String policyCategory) { this.policyCategory = policyCategory; }
    public String getVersionLabel() { return versionLabel; }
    public void setVersionLabel(String versionLabel) { this.versionLabel = versionLabel; }
    public String getContentUrl() { return contentUrl; }
    public void setContentUrl(String contentUrl) { this.contentUrl = contentUrl; }
    public String getContentHash() { return contentHash; }
    public void setContentHash(String contentHash) { this.contentHash = contentHash; }
    public String getSummaryText() { return summaryText; }
    public void setSummaryText(String summaryText) { this.summaryText = summaryText; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
