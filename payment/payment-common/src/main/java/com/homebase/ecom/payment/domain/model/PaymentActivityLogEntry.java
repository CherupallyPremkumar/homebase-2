package com.homebase.ecom.payment.domain.model;

import org.chenile.workflow.activities.model.ActivityLog;

public class PaymentActivityLogEntry implements ActivityLog {
    public String activityName;
    public String activityComment;
    public boolean activitySuccess;

    @Override public String getName() { return activityName; }
    @Override public String getComment() { return activityComment; }
    @Override public boolean getSuccess() { return activitySuccess; }
}
