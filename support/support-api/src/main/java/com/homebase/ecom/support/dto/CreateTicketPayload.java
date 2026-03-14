package com.homebase.ecom.support.dto;

import org.chenile.workflow.param.MinimalPayload;

public class CreateTicketPayload extends MinimalPayload {
    private String subject;
    private String category;
    private String priority;
    private String description;
    private String orderId;

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}
