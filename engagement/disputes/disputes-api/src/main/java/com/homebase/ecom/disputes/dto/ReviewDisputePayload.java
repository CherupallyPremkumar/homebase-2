package com.homebase.ecom.disputes.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the reviewDispute event.
 * Admin/support assigns an investigator and adds initial notes.
 */
public class ReviewDisputePayload extends MinimalPayload {

    private String assignedTo;
    private String investigationNotes;

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getInvestigationNotes() { return investigationNotes; }
    public void setInvestigationNotes(String investigationNotes) { this.investigationNotes = investigationNotes; }
}
