package com.homebase.ecom.rulesengine.query.dto;

import java.time.LocalDateTime;

public class DecisionQueryDto {
    private String id;
    private String policyId;
    private String subjectId;
    private String effect;
    private String reasons;
    private String targetModule;
    private LocalDateTime createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public String getEffect() { return effect; }
    public void setEffect(String effect) { this.effect = effect; }
    public String getReasons() { return reasons; }
    public void setReasons(String reasons) { this.reasons = reasons; }
    public String getTargetModule() { return targetModule; }
    public void setTargetModule(String targetModule) { this.targetModule = targetModule; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
}
