package com.homebase.ecom.promo.model;

import jakarta.persistence.*;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "campaign_activity")
public class CampaignActivityLog extends BaseJpaEntity implements ActivityLog {

    @Column(name = "campaign_id")
    private String campaignId;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "success")
    private boolean success = true;

    // --- ActivityLog interface ---

    @Override
    public String getName() {
        return eventId;
    }

    @Override
    public boolean getSuccess() {
        return success;
    }

    @Override
    public String getComment() {
        return comment;
    }

    // --- Getters and Setters ---

    public String getCampaignId() { return campaignId; }
    public void setCampaignId(String campaignId) { this.campaignId = campaignId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public void setComment(String comment) { this.comment = comment; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
