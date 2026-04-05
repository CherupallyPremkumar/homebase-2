package com.homebase.ecom.onboarding.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.util.List;

public class SubmitDocumentsPayload extends MinimalPayload {
    private List<String> documentTypes;
    private String documentNotes;

    public List<String> getDocumentTypes() { return documentTypes; }
    public void setDocumentTypes(List<String> documentTypes) { this.documentTypes = documentTypes; }

    public String getDocumentNotes() { return documentNotes; }
    public void setDocumentNotes(String documentNotes) { this.documentNotes = documentNotes; }
}
