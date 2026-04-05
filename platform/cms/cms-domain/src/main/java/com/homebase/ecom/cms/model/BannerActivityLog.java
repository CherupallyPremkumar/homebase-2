package com.homebase.ecom.cms.model;

import org.chenile.workflow.activities.model.ActivityLog;

import java.util.Date;

public class BannerActivityLog implements ActivityLog {
    public String actionType;
    public String activityComment;
    public boolean activitySuccess = true;
    public String performedBy;
    public Date performedAt;
    public String ipAddress;
    public String previousState;
    public String newState;

    @Override
    public String getName() { return actionType; }

    @Override
    public String getComment() { return activityComment; }

    @Override
    public boolean getSuccess() { return activitySuccess; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public Date getPerformedAt() { return performedAt; }
    public void setPerformedAt(Date performedAt) { this.performedAt = performedAt; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getPreviousState() { return previousState; }
    public void setPreviousState(String previousState) { this.previousState = previousState; }

    public String getNewState() { return newState; }
    public void setNewState(String newState) { this.newState = newState; }
}
