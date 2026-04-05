package com.homebase.ecom.reconciliation.dto;

import java.io.Serializable;
import java.util.Date;

public class ReconciliationBatchDetailQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Date batchDate;
    private Date periodStart;
    private Date periodEnd;
    private String gatewayType;
    private String reconciliationMethod;
    private int matchedCount;
    private int mismatchCount;
    private int autoResolvedCount;
    private String stateId;
    private String flowId;
    private Date createdTime;
    private Date lastModifiedTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getBatchDate() { return batchDate; }
    public void setBatchDate(Date batchDate) { this.batchDate = batchDate; }
    public Date getPeriodStart() { return periodStart; }
    public void setPeriodStart(Date periodStart) { this.periodStart = periodStart; }
    public Date getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(Date periodEnd) { this.periodEnd = periodEnd; }
    public String getGatewayType() { return gatewayType; }
    public void setGatewayType(String gatewayType) { this.gatewayType = gatewayType; }
    public String getReconciliationMethod() { return reconciliationMethod; }
    public void setReconciliationMethod(String reconciliationMethod) { this.reconciliationMethod = reconciliationMethod; }
    public int getMatchedCount() { return matchedCount; }
    public void setMatchedCount(int matchedCount) { this.matchedCount = matchedCount; }
    public int getMismatchCount() { return mismatchCount; }
    public void setMismatchCount(int mismatchCount) { this.mismatchCount = mismatchCount; }
    public int getAutoResolvedCount() { return autoResolvedCount; }
    public void setAutoResolvedCount(int autoResolvedCount) { this.autoResolvedCount = autoResolvedCount; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Date lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
}
