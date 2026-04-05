package com.homebase.ecom.fulfillment.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

@Entity
@Table(name = "fulfillment_saga_activity")
public class FulfillmentSagaActivityLogEntity extends BaseJpaEntity implements ActivityLog {

    @Column(name = "activity_name")
    public String activityName;

    @Column(name = "activity_success")
    public boolean activitySuccess;

    @Column(name = "activity_comment", length = 2000)
    public String activityComment;

    @Override
    public String getName() { return activityName; }

    @Override
    public boolean getSuccess() { return activitySuccess; }

    @Override
    public String getComment() { return activityComment; }
}
