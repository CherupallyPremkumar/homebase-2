package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the resolveFromProbation event (ON_PROBATION -> ACTIVE).
 * Admin confirms supplier has improved performance metrics.
 */
public class ResolveFromProbationPayload extends MinimalPayload {

    private String resolutionNotes;

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
}
