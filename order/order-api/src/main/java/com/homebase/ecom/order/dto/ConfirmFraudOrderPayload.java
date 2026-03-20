package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for confirmFraud event -- admin confirms order is fraudulent.
 */
public class ConfirmFraudOrderPayload extends MinimalPayload {
    private String fraudType;
    private String fraudEvidence;

    public String getFraudType() { return fraudType; }
    public void setFraudType(String fraudType) { this.fraudType = fraudType; }

    public String getFraudEvidence() { return fraudEvidence; }
    public void setFraudEvidence(String fraudEvidence) { this.fraudEvidence = fraudEvidence; }
}
