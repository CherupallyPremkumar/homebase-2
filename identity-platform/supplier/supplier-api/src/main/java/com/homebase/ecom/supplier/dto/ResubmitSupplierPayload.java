package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the resubmitSupplier event.
 * Supplier resubmits after rejection with updated documentation.
 */
public class ResubmitSupplierPayload extends MinimalPayload {

    private String updatedDocumentationNotes;

    public String getUpdatedDocumentationNotes() { return updatedDocumentationNotes; }
    public void setUpdatedDocumentationNotes(String updatedDocumentationNotes) {
        this.updatedDocumentationNotes = updatedDocumentationNotes;
    }
}
