package com.homebase.ecom.onboarding.model;

import org.chenile.workflow.activities.model.ActivityLog;

public class OnboardingSagaActivityLog implements ActivityLog {
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
