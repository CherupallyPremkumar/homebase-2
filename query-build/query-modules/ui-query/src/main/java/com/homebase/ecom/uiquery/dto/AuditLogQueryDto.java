package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.util.Date;

public class AuditLogQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String entityType;
    private String entityId;
    private String action;
    private String oldValue;
    private String newValue;
    private String changedBy;
    private String ipAddress;
    private String correlationId;
    private boolean isCritical;
    private String details;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public boolean getIsCritical() { return isCritical; }
    public void setIsCritical(boolean isCritical) { this.isCritical = isCritical; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
