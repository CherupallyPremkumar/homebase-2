package com.homebase.ecom.notification.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

public class Notification extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String id;
    private String userId;
    private String channel;
    private String templateCode;
    private String subject;
    private String body;
    private String referenceType;
    private String referenceId;
    private Date readAt;
    private Date sentAt;
    private String errorMessage;
    private int retryCount;

    // Workflow related
    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // Getters and Setters
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

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        NotificationActivityLog activityLog = new NotificationActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        this.activities.add(activityLog);
        return activityLog;
    }
}
