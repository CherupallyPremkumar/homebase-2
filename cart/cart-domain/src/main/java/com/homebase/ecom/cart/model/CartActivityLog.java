package com.homebase.ecom.cart.model;

import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.BaseEntity;
import java.util.Date;

public class CartActivityLog extends BaseEntity implements ActivityLog {
    public String activityName;
    public boolean activitySuccess;
    public String activityComment;

    public String getName() {
        return activityName;
    }

    public boolean getSuccess() {
        return activitySuccess;
    }

    public String getComment() {
        return activityComment;
    }
}
