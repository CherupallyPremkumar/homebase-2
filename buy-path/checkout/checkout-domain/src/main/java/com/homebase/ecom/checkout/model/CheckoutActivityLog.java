package com.homebase.ecom.checkout.model;

import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.BaseEntity;

public class CheckoutActivityLog extends BaseEntity implements ActivityLog {
    public String activityName;
    public boolean activitySuccess;
    public String activityComment;
    private String tenant;

    @Override
    public String getName() {
        return activityName;
    }

    @Override
    public boolean getSuccess() {
        return activitySuccess;
    }

    @Override
    public String getComment() {
        return activityComment;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
