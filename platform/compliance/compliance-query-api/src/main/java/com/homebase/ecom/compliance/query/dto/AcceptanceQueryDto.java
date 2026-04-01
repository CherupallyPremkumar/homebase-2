package com.homebase.ecom.compliance.query.dto;

import java.time.LocalDateTime;

public class AcceptanceQueryDto {
    private String id;
    private String agreementTitle;
    private String userId;
    private String userType;
    private LocalDateTime acceptedAt;
    private String acceptanceMethod;
    private boolean isCurrent;
    private String agreementId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAgreementTitle() { return agreementTitle; }
    public void setAgreementTitle(String agreementTitle) { this.agreementTitle = agreementTitle; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
    public String getAcceptanceMethod() { return acceptanceMethod; }
    public void setAcceptanceMethod(String acceptanceMethod) { this.acceptanceMethod = acceptanceMethod; }
    public boolean isCurrent() { return isCurrent; }
    public void setCurrent(boolean current) { isCurrent = current; }
    public String getAgreementId() { return agreementId; }
    public void setAgreementId(String agreementId) { this.agreementId = agreementId; }
}
