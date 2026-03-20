package com.homebase.ecom.compliance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

@Entity
@Table(name = "agreement_activity")
public class AgreementActivityEntity extends BaseJpaEntity implements ActivityLog {

    @Column(name = "agreement_id", insertable = false, updatable = false)
    private String agreementId;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_success")
    private boolean activitySuccess;

    @Column(name = "activity_comment", length = 1000)
    private String activityComment;

    public String getAgreementId() { return agreementId; }
    public void setAgreementId(String agreementId) { this.agreementId = agreementId; }
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
