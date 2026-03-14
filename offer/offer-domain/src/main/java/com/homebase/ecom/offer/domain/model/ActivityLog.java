package com.homebase.ecom.offer.domain.model;

import java.io.Serializable;

public class ActivityLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eventId;
    private String comment;

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
