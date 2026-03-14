package com.homebase.ecom.product.model;

import org.chenile.workflow.activities.model.ActivityLog;

public class ProductActivityLog implements ActivityLog {
    public String activityName;
    public boolean activitySuccess;
    public String activityComment;

    public void setActivityName(String activityName) { this.activityName = activityName; }
    public void setActivitySuccess(boolean activitySuccess) { this.activitySuccess = activitySuccess; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }

    @Override
    public String getName() { return activityName; }

    @Override
    public boolean getSuccess() { return activitySuccess; }

    @Override
    public String getComment() { return activityComment; }
}
