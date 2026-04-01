package com.homebase.ecom.disputes.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the escalateDispute event.
 * Escalates the dispute to a higher priority or supervisor.
 */
public class EscalateDisputePayload extends MinimalPayload {

    private String escalationReason;
    private String priority;

    public String getEscalationReason() { return escalationReason; }
    public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
