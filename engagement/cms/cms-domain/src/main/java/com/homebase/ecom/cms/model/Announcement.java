package com.homebase.ecom.cms.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

import java.time.LocalDateTime;
import java.util.*;

public class Announcement extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    // Existing fields
    private String title;
    private String message;
    private String announcementType;
    private String targetAudience;
    private String priority;
    private String status;
    private LocalDateTime scheduledDate;
    private LocalDateTime expiryDate;
    private String actionUrl;
    private String actionLabel;
    private boolean dismissible;
    private String tenant;

    // STM workflow support
    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public TransientMap getTransientMap() { return this.transientMap; }

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        AnnouncementActivityLog activityLog = new AnnouncementActivityLog();
        activityLog.actionType = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }

    // Getters and setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getAnnouncementType() { return announcementType; }
    public void setAnnouncementType(String announcementType) { this.announcementType = announcementType; }

    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }

    public String getActionLabel() { return actionLabel; }
    public void setActionLabel(String actionLabel) { this.actionLabel = actionLabel; }

    public boolean isDismissible() { return dismissible; }
    public void setDismissible(boolean dismissible) { this.dismissible = dismissible; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
