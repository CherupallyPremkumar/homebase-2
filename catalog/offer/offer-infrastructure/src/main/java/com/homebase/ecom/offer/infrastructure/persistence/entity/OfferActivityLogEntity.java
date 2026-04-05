package com.homebase.ecom.offer.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "offer_activity_log")
public class OfferActivityLogEntity extends BaseJpaEntity {

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "offer_id")
    private String offerId;

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getOfferId() { return offerId; }
    public void setOfferId(String offerId) { this.offerId = offerId; }
}
