package com.homebase.ecom.promo.model;

import jakarta.persistence.*;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "promo_code_activity_log")
public class PromoCodeActivityLog extends BaseJpaEntity implements ActivityLog {

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_success")
    private boolean activitySuccess;

    @Column(name = "activity_comment")
    private String activityComment;

    @Column(name = "promo_code")
    private String promoCode;

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setActivitySuccess(boolean activitySuccess) {
        this.activitySuccess = activitySuccess;
    }

    public void setActivityComment(String activityComment) {
        this.activityComment = activityComment;
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

    // Getters and setters
    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getActivityName() {
        return activityName;
    }

    public boolean isActivitySuccess() {
        return activitySuccess;
    }

    public String getActivityComment() {
        return activityComment;
    }
}
