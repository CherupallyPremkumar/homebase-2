package com.homebase.ecom.compliance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

@Entity
@Table(name = "platform_policy_activity")
public class PlatformPolicyActivityEntity extends BaseJpaEntity implements ActivityLog {

    @Column(name = "policy_id", insertable = false, updatable = false)
    private String policyId;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_success")
    private boolean activitySuccess;

    @Column(name = "activity_comment", length = 1000)
    private String activityComment;

    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }
    @Override public String getName() { return activityName; }
    @Override public boolean getSuccess() { return activitySuccess; }
    @Override public String getComment() { return activityComment; }

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public boolean isActivitySuccess() { return activitySuccess; }
    public void setActivitySuccess(boolean activitySuccess) { this.activitySuccess = activitySuccess; }
    public String getActivityComment() { return activityComment; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }
}
