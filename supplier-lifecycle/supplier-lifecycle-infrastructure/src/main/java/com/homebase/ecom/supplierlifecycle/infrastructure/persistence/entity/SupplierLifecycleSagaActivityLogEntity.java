package com.homebase.ecom.supplierlifecycle.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

/**
 * JPA entity for supplier_lifecycle_saga_activity table.
 * DB columns: activity_name, activity_success, activity_comment
 * (plus BaseJpaEntity columns: id, created_time, last_modified_time, etc.)
 */
@Entity
@Table(name = "supplier_lifecycle_saga_activity")
public class SupplierLifecycleSagaActivityLogEntity extends BaseJpaEntity implements ActivityLog {

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_success")
    private boolean activitySuccess = true;

    @Column(name = "activity_comment", length = 2000)
    private String activityComment;

    @Override
    public String getName() { return activityName; }

    @Override
    public String getComment() { return activityComment; }

    @Override
    public boolean getSuccess() { return activitySuccess; }

    // Setters
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    public boolean isActivitySuccess() { return activitySuccess; }
    public void setActivitySuccess(boolean activitySuccess) { this.activitySuccess = activitySuccess; }

    public String getActivityComment() { return activityComment; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }
}
