package com.homebase.ecom.settlement.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "settlement_activity")
public class SettlementActivityLog extends BaseJpaEntity implements ActivityLog, Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "activity_name", nullable = false)
    public String activityName;

    @Column(name = "activity_success", nullable = false)
    public boolean activitySuccess;

    @Column(name = "activity_comment", columnDefinition = "TEXT")
    public String activityComment;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @Override
    public String getEventId() {
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
