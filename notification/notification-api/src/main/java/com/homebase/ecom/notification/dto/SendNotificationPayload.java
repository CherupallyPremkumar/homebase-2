package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the send event in the notification STM.
 */
public class SendNotificationPayload extends MinimalPayload {
    private String channel;
    private String templateCode;
    private String subject;
    private String body;
    private String referenceType;
    private String referenceId;

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
}
