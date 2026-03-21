package com.homebase.ecom.payment.exception;

/**
 * Exception thrown when a duplicate webhook event is detected.
 * This is used for idempotency - the same webhook event should only
 * be processed once.
 */
public class DuplicateWebhookException extends RuntimeException {

    private final String eventId;

    public DuplicateWebhookException(String eventId) {
        super("Duplicate webhook event: " + eventId);
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
