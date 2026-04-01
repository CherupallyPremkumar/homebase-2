package com.homebase.ecom.notification.domain.model;

/**
 * Template for generating notification content.
 */
public class NotificationTemplate {
    private String id;
    private String code;
    private String channel;
    private String subjectTemplate;
    private String bodyTemplate;
    private boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getSubjectTemplate() { return subjectTemplate; }
    public void setSubjectTemplate(String subjectTemplate) { this.subjectTemplate = subjectTemplate; }

    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
