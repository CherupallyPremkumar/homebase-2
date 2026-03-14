package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the inspectReturn event.
 * Carries inspector details and notes from quality checker review.
 */
public class InspectReturnReturnrequestPayload extends MinimalPayload {

    private String inspectorId;
    private String inspectorNotes;

    public String getInspectorId() { return inspectorId; }
    public void setInspectorId(String inspectorId) { this.inspectorId = inspectorId; }

    public String getInspectorNotes() { return inspectorNotes; }
    public void setInspectorNotes(String inspectorNotes) { this.inspectorNotes = inspectorNotes; }
}
