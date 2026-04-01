package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Versioned envelope for Kafka events.
 *
 * This allows evolving payload schemas while keeping a stable outer contract.
 */
public class EventEnvelope implements Serializable {

    private String eventType;
    private int version;
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Event payload. When consumed, this will typically deserialize to a Map and
     * can be converted to a concrete DTO via ObjectMapper.convertValue(...).
     */
    private Object payload;

    public EventEnvelope() {
    }

    public EventEnvelope(String eventType, int version, LocalDateTime timestamp, Object payload) {
        this.eventType = eventType;
        this.version = version;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public static EventEnvelope of(String eventType, int version, Object payload) {
        return new EventEnvelope(eventType, version, LocalDateTime.now(), payload);
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
