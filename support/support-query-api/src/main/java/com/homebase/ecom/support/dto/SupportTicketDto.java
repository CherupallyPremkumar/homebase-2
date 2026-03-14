package com.homebase.ecom.support.dto;

import java.io.Serializable;
import java.util.Date;

public class SupportTicketDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String subject;
    private String category;
    private String priority;
    private String assignedTo;
    private String status;
    private String stateId;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
