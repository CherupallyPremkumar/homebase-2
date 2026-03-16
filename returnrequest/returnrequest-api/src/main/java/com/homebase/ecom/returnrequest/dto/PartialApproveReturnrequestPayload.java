package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.math.BigDecimal;
import java.util.List;

/**
 * Payload for the partialApprove event (UNDER_REVIEW -> PARTIALLY_APPROVED).
 * Allows approving only certain items from the return request.
 */
public class PartialApproveReturnrequestPayload extends MinimalPayload {

    private List<String> approvedItemIds;
    private BigDecimal adjustedRefundAmount;
    private String notes;

    public List<String> getApprovedItemIds() { return approvedItemIds; }
    public void setApprovedItemIds(List<String> approvedItemIds) { this.approvedItemIds = approvedItemIds; }

    public BigDecimal getAdjustedRefundAmount() { return adjustedRefundAmount; }
    public void setAdjustedRefundAmount(BigDecimal adjustedRefundAmount) { this.adjustedRefundAmount = adjustedRefundAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
