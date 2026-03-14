package com.homebase.ecom.support.dto;

import org.chenile.workflow.param.MinimalPayload;

public class AssignAgentPayload extends MinimalPayload {
    private String agentId;

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
}
