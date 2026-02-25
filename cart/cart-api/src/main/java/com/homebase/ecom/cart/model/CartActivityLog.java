package com.homebase.ecom.cart.model;

import org.chenile.workflow.activities.model.ActivityLog;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;
@Entity
@Table(name = "cart_activity")
public class CartActivityLog extends BaseJpaEntity implements ActivityLog{
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
