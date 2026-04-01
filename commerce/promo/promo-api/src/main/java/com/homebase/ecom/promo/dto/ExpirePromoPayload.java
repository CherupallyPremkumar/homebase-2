package com.homebase.ecom.promo.dto;

import org.chenile.workflow.param.MinimalPayload;

public class ExpirePromoPayload extends MinimalPayload {
    private String reason;
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
