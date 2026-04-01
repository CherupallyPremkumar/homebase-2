package com.homebase.ecom.disputes.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "dispute_activity")
public class DisputeActivityLogEntity extends BaseJpaEntity implements ActivityLog {

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "success")
    private boolean success = true;

    @Override
    public String getName() { return eventId; }

    @Override
    public String getComment() { return comment; }

    @Override
    public boolean getSuccess() { return success; }

    // Setters
    public void setEventId(String eventId) { this.eventId = eventId; }
    public void setComment(String comment) { this.comment = comment; }
    public void setSuccess(boolean success) { this.success = success; }
}
