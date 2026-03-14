package com.homebase.ecom.fulfillment.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

@Entity
@Table(name = "fulfillment_saga_activity")
public class FulfillmentSagaActivityLogEntity extends BaseJpaEntity implements ActivityLog {
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
