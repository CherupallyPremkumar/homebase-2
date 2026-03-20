package com.homebase.ecom.reconciliation.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

/**
 * JPA entity for reconciliation_activity table.
 */
@Entity
@Table(name = "reconciliation_activity")
public class ReconciliationBatchActivityLogEntity extends BaseJpaEntity {

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_success")
    private boolean activitySuccess;

    @Column(name = "activity_comment", length = 2000)
    private String activityComment;

    // --- Getters & Setters ---

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    public boolean isActivitySuccess() { return activitySuccess; }
    public void setActivitySuccess(boolean activitySuccess) { this.activitySuccess = activitySuccess; }

    public String getActivityComment() { return activityComment; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }
}
