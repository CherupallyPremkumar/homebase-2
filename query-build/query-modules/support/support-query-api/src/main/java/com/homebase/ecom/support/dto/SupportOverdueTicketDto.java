package com.homebase.ecom.support.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Query DTO for the getOverdueTickets query, which includes a computed
 * hoursOpen field representing the number of hours since ticket creation.
 */
public class SupportOverdueTicketDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String customerId;
    private String subject;
    private String category;
    private String priority;
    private String stateId;
    private String flowId;
    private String assignedAgentId;
    private Date createdAt;
    private BigDecimal hoursOpen;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
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
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public BigDecimal getHoursOpen() { return hoursOpen; }
    public void setHoursOpen(BigDecimal hoursOpen) { this.hoursOpen = hoursOpen; }
}
