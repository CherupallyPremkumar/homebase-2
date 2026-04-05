package com.homebase.ecom.onboarding.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Query DTO for onboarding list views: getAll, getPendingReview, getBySupplier.
 * Fields are a superset of all three queries; MyBatis sets only the columns
 * present in each SELECT (others remain null).
 */
public class OnboardingQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String supplierId;
    private String applicantName;
    private String businessName;
    private String businessType;
    private int trainingProgress;
    private String stateId;
    private String flowId;
    private Date submittedAt;
    private Date createdTime;
    private Date lastModifiedTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public int getTrainingProgress() { return trainingProgress; }
    public void setTrainingProgress(int trainingProgress) { this.trainingProgress = trainingProgress; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public Date getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Date submittedAt) { this.submittedAt = submittedAt; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Date lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
}
