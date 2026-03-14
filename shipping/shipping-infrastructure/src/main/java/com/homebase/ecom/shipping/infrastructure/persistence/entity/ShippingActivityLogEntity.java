package com.homebase.ecom.shipping.infrastructure.persistence.entity;

import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.jpautils.entity.BaseJpaEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "shipping_activity")
public class ShippingActivityLogEntity extends BaseJpaEntity implements ActivityLog {
    public String activityName;
    public boolean activitySuccess;
    public String activityComment;

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
}
