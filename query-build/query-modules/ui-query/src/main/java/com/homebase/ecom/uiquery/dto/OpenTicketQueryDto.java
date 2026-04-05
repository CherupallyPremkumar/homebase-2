package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.util.Date;

public class OpenTicketQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ticketId;
    private String ticketState;
    private String subject;
    private String priority;
    private String customerId;
    private String orderId;
    private Date createdTime;
    private String assignedTo;

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public String getTicketState() { return ticketState; }
    public void setTicketState(String ticketState) { this.ticketState = ticketState; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
}
