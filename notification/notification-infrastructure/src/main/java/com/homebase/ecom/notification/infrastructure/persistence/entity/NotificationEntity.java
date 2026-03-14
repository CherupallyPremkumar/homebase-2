package com.homebase.ecom.notification.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class NotificationEntity extends AbstractJpaStateEntity {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "channel")
    private String channel;

    @Column(name = "template_code")
    private String templateCode;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "read_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date readAt;

    @Column(name = "sent_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "retry_count")
    private int retryCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "notification_id")
    private List<NotificationActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

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

    public Date getReadAt() { return readAt; }
    public void setReadAt(Date readAt) { this.readAt = readAt; }

    public Date getSentAt() { return sentAt; }
    public void setSentAt(Date sentAt) { this.sentAt = sentAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public List<NotificationActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<NotificationActivityLogEntity> activities) { this.activities = activities; }
}
