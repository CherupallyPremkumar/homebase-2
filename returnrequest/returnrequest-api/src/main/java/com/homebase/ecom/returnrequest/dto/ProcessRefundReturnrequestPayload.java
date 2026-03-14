package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;
import java.math.BigDecimal;

/**
 * Payload for the processRefund event.
 * Carries refund amount and method details.
 */
public class ProcessRefundReturnrequestPayload extends MinimalPayload {

    private BigDecimal refundAmount;
    private String refundMethod;

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
}
