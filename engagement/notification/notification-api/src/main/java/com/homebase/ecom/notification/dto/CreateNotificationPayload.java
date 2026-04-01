package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for creating a notification (initial entity creation).
 */
public class CreateNotificationPayload extends MinimalPayload {
    private String customerId;
    private String channel;
    private String templateId;
    private String subject;
    private String body;
    private String recipientAddress;
    private java.util.Map<String, String> metadata;

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getRecipientAddress() { return recipientAddress; }
    public void setRecipientAddress(String recipientAddress) { this.recipientAddress = recipientAddress; }

    public java.util.Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(java.util.Map<String, String> metadata) { this.metadata = metadata; }
}
