package com.homebase.ecom.onboarding.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.util.List;

/**
 * Payload for requestDocuments event — admin requests additional documents from seller.
 */
public class RequestDocumentsPayload extends MinimalPayload {
    private List<String> requestedDocumentTypes;
    private String notes;

    public List<String> getRequestedDocumentTypes() { return requestedDocumentTypes; }
    public void setRequestedDocumentTypes(List<String> requestedDocumentTypes) { this.requestedDocumentTypes = requestedDocumentTypes; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
