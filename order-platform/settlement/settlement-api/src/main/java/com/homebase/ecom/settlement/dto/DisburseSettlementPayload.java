package com.homebase.ecom.settlement.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the disburse event.
 */
public class DisburseSettlementPayload extends MinimalPayload {

    private String disbursementMethod;

    public String getDisbursementMethod() { return disbursementMethod; }
    public void setDisbursementMethod(String disbursementMethod) { this.disbursementMethod = disbursementMethod; }
}
