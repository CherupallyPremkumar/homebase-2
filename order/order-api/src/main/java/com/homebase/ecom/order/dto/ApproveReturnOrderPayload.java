package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.util.List;

/**
 * Customized Payload for the approveReturn event.
 */
public class ApproveReturnOrderPayload extends MinimalPayload {
    private String approvedBy;
    private List<String> approvedItemIds;

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public List<String> getApprovedItemIds() {
        return approvedItemIds;
    }

    public void setApprovedItemIds(List<String> approvedItemIds) {
        this.approvedItemIds = approvedItemIds;
    }
}
