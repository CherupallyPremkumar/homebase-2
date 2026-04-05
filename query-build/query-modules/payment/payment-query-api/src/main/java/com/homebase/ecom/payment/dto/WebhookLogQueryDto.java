package com.homebase.ecom.payment.dto;

import java.io.Serializable;
import java.util.Date;

public class WebhookLogQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String paymentId;
    private String eventType;
    private String provider;
    private String status;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
