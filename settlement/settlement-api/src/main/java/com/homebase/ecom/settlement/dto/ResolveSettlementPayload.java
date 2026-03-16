package com.homebase.ecom.settlement.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the resolve event (dispute resolution).
 */
public class ResolveSettlementPayload extends MinimalPayload {

    private String resolution;

    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
}
