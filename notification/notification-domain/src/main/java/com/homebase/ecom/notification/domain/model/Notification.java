package com.homebase.ecom.notification.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

/**
 * Notification domain model.
 *
 * Maps to DB table: notifications (see db-migrations notification changelog).
 * Fields align 1:1 with DB columns including changeset 006 additions
 * (referenceType, referenceId, priority, scheduledAt, openedAt, clickedAt).
 *
 * No order/product/payment details beyond reference — those belong in the triggering event metadata.
 */
public class Notification extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    // id, createdTime, lastModifiedTime, createdBy, lastModifiedBy, version inherited from BaseEntity
    // stateEntryTime, slaTendingLate, slaLate inherited from AbstractExtendedStateEntity

    private String customerId;
    private String channel;       // EMAIL, SMS, PUSH, IN_APP
    private String templateId;
    private String subject;
    private String body;
    private String recipientAddress;
    private Map<String, String> metadata = new HashMap<>();
    private Date sentAt;
    private Date deliveredAt;
    private String failureReason;
    private int retryCount;
    private String tenant;

    // Changeset 006 columns: reference, priority, scheduling, tracking
    private String referenceType;
    private String referenceId;
    private String priority;      // NORMAL, HIGH, LOW
    private Date scheduledAt;
    private Date openedAt;
    private Date clickedAt;

    // Workflow related
    private transient TransientMap transientMap = new TransientMap();
    private List<NotificationActivityLog> activities = new ArrayList<>();

    public String description;

    // Getters and Setters

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

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    public Date getSentAt() { return sentAt; }
    public void setSentAt(Date sentAt) { this.sentAt = sentAt; }

    public Date getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Date deliveredAt) { this.deliveredAt = deliveredAt; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Date getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(Date scheduledAt) { this.scheduledAt = scheduledAt; }

    public Date getOpenedAt() { return openedAt; }
    public void setOpenedAt(Date openedAt) { this.openedAt = openedAt; }

    public Date getClickedAt() { return clickedAt; }
    public void setClickedAt(Date clickedAt) { this.clickedAt = clickedAt; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    public List<NotificationActivityLog> getActivities() { return activities; }
    public void setActivities(List<NotificationActivityLog> activities) { this.activities = activities; }

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

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
