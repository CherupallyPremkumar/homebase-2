package com.homebase.ecom.user.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

/**
 * JPA Entity for UserActivityLog.
 * Maps to user_activity_log table.
 */
@Entity
@Table(name = "user_activity_log")
public class UserActivityLogEntity extends BaseJpaEntity implements ActivityLog {

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_success")
    private boolean activitySuccess;

    @Column(name = "activity_comment")
    private String activityComment;

    @Override
    public String getName() {
        return activityName;
    }

    public void setName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public boolean getSuccess() {
        return activitySuccess;
    }

    public void setSuccess(boolean activitySuccess) {
        this.activitySuccess = activitySuccess;
    }

    @Override
    public String getComment() {
        return activityComment;
    }

    public void setComment(String activityComment) {
        this.activityComment = activityComment;
    }
}
