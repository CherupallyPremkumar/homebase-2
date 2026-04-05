package com.homebase.ecom.cms.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the reject event on a CmsPage.
 * Carries a rejectionReason explaining why the page was sent back to DRAFT.
 */
public class RejectCmsPagePayload extends MinimalPayload {

    private String rejectionReason;

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
