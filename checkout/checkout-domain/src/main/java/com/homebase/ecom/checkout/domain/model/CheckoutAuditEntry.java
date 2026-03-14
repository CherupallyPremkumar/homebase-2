package com.homebase.ecom.checkout.domain.model;

import java.time.LocalDateTime;

public class CheckoutAuditEntry {
    private final String action;
    private final CheckoutState fromState;
    private final CheckoutState toState;
    private final LocalDateTime timestamp;

    public CheckoutAuditEntry(String action, CheckoutState fromState, CheckoutState toState, LocalDateTime timestamp) {
        this.action = action;
        this.fromState = fromState;
        this.toState = toState;
        this.timestamp = timestamp;
    }

    public String getAction() { return action; }
    public CheckoutState getFromState() { return fromState; }
    public CheckoutState getToState() { return toState; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
