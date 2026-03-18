package com.homebase.ecom.user.domain.model;

import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.BaseEntity;

/**
 * UserActivityLog — records every STM lifecycle event for audit purposes.
 *
 * Pure domain object — no JPA annotations.
 * The JPA version (UserActivityLogEntity) lives in user-infrastructure.
 *
 * Follows same pattern as CartActivityLog.
 */
public class UserActivityLog extends BaseEntity implements ActivityLog {

    public String activityName;
    public boolean activitySuccess;
    public String activityComment;
    private String tenant;

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    public boolean isActivitySuccess() { return activitySuccess; }
    public void setActivitySuccess(boolean activitySuccess) { this.activitySuccess = activitySuccess; }

    public String getActivityComment() { return activityComment; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }

    @Override
    public String getName() { return activityName; }

    @Override
    public boolean getSuccess() { return activitySuccess; }

    @Override
    public String getComment() { return activityComment; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
