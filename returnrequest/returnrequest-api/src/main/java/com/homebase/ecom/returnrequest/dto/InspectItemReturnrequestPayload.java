package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the inspectItem event (ITEM_RECEIVED -> INSPECTED).
 * Warehouse inspector verifies item condition.
 */
public class InspectItemReturnrequestPayload extends MinimalPayload {

    private String inspectorId;
    private String inspectorNotes;
    private String verifiedCondition;

    public String getInspectorId() { return inspectorId; }
    public void setInspectorId(String inspectorId) { this.inspectorId = inspectorId; }

    public String getInspectorNotes() { return inspectorNotes; }
    public void setInspectorNotes(String inspectorNotes) { this.inspectorNotes = inspectorNotes; }

    public String getVerifiedCondition() { return verifiedCondition; }
    public void setVerifiedCondition(String verifiedCondition) { this.verifiedCondition = verifiedCondition; }
}
