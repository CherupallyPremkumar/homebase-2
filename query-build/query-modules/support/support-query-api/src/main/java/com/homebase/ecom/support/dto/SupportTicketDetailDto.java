package com.homebase.ecom.support.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Query DTO for support ticket detail views: getById, getByCustomerId.
 * Superset of the list DTO, adding orderId, description, and resolvedAt.
 */
public class SupportTicketDetailDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String customerId;
    private String orderId;
    private String subject;
    private String category;
    private String priority;
    private String stateId;
    private String flowId;
    private String assignedAgentId;
    private String description;
    private Date resolvedAt;
    private int reopenCount;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public String getAssignedAgentId() { return assignedAgentId; }
    public void setAssignedAgentId(String assignedAgentId) { this.assignedAgentId = assignedAgentId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }
    public int getReopenCount() { return reopenCount; }
    public void setReopenCount(int reopenCount) { this.reopenCount = reopenCount; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
