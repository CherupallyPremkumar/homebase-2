package com.homebase.ecom.payment.domain;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

@Entity
@Table(name = "payment_activity")
public class PaymentActivityLog extends BaseJpaEntity implements ActivityLog {

    @Column(name = "activity_name")
    public String activityName;

    @Column(name = "activity_success")
    public boolean activitySuccess;

    @Column(name = "activity_comment")
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
