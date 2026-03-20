package com.homebase.ecom.rulesengine.query.dto;

import java.time.LocalDateTime;

public class RuleSetQueryDto {
    private String id;
    private String name;
    private String description;
    private String targetModule;
    private String defaultEffect;
    private boolean active;
    private String stateId;
    private String flowId;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private String tenant;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTargetModule() { return targetModule; }
    public void setTargetModule(String targetModule) { this.targetModule = targetModule; }
    public String getDefaultEffect() { return defaultEffect; }
    public void setDefaultEffect(String defaultEffect) { this.defaultEffect = defaultEffect; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(LocalDateTime lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
