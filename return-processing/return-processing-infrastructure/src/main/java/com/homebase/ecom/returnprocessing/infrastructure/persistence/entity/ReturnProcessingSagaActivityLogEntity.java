package com.homebase.ecom.returnprocessing.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "return_processing_saga_activity")
public class ReturnProcessingSagaActivityLogEntity extends BaseJpaEntity {

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_success")
    private boolean activitySuccess;

    @Column(name = "activity_comment")
    private String activityComment;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public boolean isActivitySuccess() {
        return activitySuccess;
    }

    public void setActivitySuccess(boolean activitySuccess) {
        this.activitySuccess = activitySuccess;
    }

    public String getActivityComment() {
        return activityComment;
    }

    public void setActivityComment(String activityComment) {
        this.activityComment = activityComment;
    }
}
