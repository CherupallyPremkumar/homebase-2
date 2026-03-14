package com.homebase.ecom.onboarding.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "onboarding_activity")
public class OnboardingSagaActivityLogEntity extends BaseJpaEntity implements ActivityLog {

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_comment")
    private String activityComment;

    @Column(name = "activity_success")
    private boolean activitySuccess = true;

    @Override
    public String getName() { return activityName; }

    @Override
    public String getComment() { return activityComment; }

    @Override
    public boolean getSuccess() { return activitySuccess; }

    // Setters
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }
    public void setActivitySuccess(boolean activitySuccess) { this.activitySuccess = activitySuccess; }
}
