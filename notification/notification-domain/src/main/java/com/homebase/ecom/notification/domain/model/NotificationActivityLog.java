package com.homebase.ecom.notification.domain.model;

import org.chenile.workflow.activities.model.ActivityLog;

public class NotificationActivityLog implements ActivityLog {
    public String activityName;
    public boolean activitySuccess;
    public String activityComment;

    @Override
    public String getName() { return activityName; }
    @Override
    public boolean getSuccess() { return activitySuccess; }
    @Override
    public String getComment() { return activityComment; }
}
