package com.homebase.ecom.policy.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

@Entity
@Table(name = "policy_activity_log")
public class PolicyActivityLogEntity extends BaseJpaEntity implements ActivityLog {

    @Column(name = "activity_name")
    private String name;

    @Column(name = "activity_success")
    private boolean success;

    @Column(name = "activity_comment")
    private String comment;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
