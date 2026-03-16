package com.homebase.ecom.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_activity_log")
public class PaymentActivityLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "success")
    private Boolean success;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getName() { return eventId; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
}
