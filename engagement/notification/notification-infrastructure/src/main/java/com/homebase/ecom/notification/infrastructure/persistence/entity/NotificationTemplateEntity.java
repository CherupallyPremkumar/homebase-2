package com.homebase.ecom.notification.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "notification_templates")
public class NotificationTemplateEntity extends BaseJpaEntity {

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "channel")
    private String channel;

    @Column(name = "subject_template")
    private String subjectTemplate;

    @Column(name = "body_template", columnDefinition = "TEXT")
    private String bodyTemplate;

    @Column(name = "active")
    private boolean active = true;

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
