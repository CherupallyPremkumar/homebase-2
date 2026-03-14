package com.homebase.ecom.notification.dto;

/**
 * Canonical response DTO for the "notifications" and "notification" queries.
 *
 * Field names MUST match the SQL column aliases defined in notification.xml.
 */
public class NotificationDto {

    private String id;
    private String userId;
    private String channel;
    private String templateCode;
    private String subject;
    private String referenceType;
    private String referenceId;
    private String sentAt;
    private String readAt;
    private int retryCount;
    private String stateId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }

    public String getSentAt() { return sentAt; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }

    public String getReadAt() { return readAt; }
    public void setReadAt(String readAt) { this.readAt = readAt; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
}
