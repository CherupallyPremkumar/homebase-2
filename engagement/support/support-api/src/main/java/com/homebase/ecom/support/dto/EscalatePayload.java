package com.homebase.ecom.support.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for escalating a support ticket (ASSIGNED -> ESCALATED).
 */
public class EscalatePayload extends MinimalPayload {
    private String escalationReason;
    private String escalateTo;

    public String getEscalationReason() { return escalationReason; }
    public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }

    public String getEscalateTo() { return escalateTo; }
    public void setEscalateTo(String escalateTo) { this.escalateTo = escalateTo; }
}
