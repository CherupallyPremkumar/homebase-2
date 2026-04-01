package com.homebase.ecom.settlement.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "settlement_activity")
public class SettlementActivityLogEntity extends BaseJpaEntity implements ActivityLog, Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @Column(name = "activity_success", nullable = false)
    private boolean activitySuccess;

    @Column(name = "activity_comment", columnDefinition = "TEXT")
    private String activityComment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    // --- Getters & Setters ---

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

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
