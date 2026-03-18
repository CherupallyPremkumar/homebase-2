package com.homebase.ecom.product.domain.model;

import org.chenile.utils.entity.model.BaseEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.io.Serial;
import java.io.Serializable;

public class ProductActivityLog extends BaseEntity implements ActivityLog, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String activityName;
    public String activityComment;
    public boolean activitySuccess;
    private String tenant;

    public void setActivityName(String activityName) { this.activityName = activityName; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }
    public void setActivitySuccess(boolean activitySuccess) { this.activitySuccess = activitySuccess; }

    @Override
    public String getName() { return activityName; }

    @Override
    public boolean getSuccess() { return activitySuccess; }

    @Override
    public String getComment() { return activityComment; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
