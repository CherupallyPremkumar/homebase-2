package com.homebase.ecom.support.dto;

import org.chenile.workflow.param.MinimalPayload;

public class ResolveTicketPayload extends MinimalPayload {
    private String resolution;

    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
}
