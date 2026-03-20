package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the escalate event (UNDER_REVIEW -> ESCALATED).
 * Customer or support escalates for supervisor review.
 */
public class EscalateReturnPayload extends MinimalPayload {

    private String escalationReason;

    public String getEscalationReason() { return escalationReason; }
    public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }
}
