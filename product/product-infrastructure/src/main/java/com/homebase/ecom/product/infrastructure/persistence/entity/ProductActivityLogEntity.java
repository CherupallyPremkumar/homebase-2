package com.homebase.ecom.product.infrastructure.persistence.entity;

import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_activity_log")
public class ProductActivityLogEntity extends BaseJpaEntity implements ActivityLog, Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "activity_name")
    public String activityName;

    @Column(name = "activity_comment", length = 2000)
    public String activityComment;

    @Column(name = "activity_success")
    public boolean activitySuccess;

    public void setActivityName(String activityName) { this.activityName = activityName; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }
    public void setActivitySuccess(boolean activitySuccess) { this.activitySuccess = activitySuccess; }

    @Override
    public String getName() { return activityName; }

    @Override
    public boolean getSuccess() { return activitySuccess; }

    @Override
    public String getComment() { return activityComment; }
}
