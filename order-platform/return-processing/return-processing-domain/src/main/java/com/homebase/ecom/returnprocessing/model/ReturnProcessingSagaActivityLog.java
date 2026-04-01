package com.homebase.ecom.returnprocessing.model;

import org.chenile.utils.entity.model.BaseEntity;
import org.chenile.workflow.activities.model.ActivityLog;

/**
 * Activity log entry for return processing saga state transitions.
 */
public class ReturnProcessingSagaActivityLog extends BaseEntity implements ActivityLog {

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

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
