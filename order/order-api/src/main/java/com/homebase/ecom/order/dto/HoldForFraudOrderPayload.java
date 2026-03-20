package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for holdForFraud event -- triggered when risk score exceeds threshold.
 */
public class HoldForFraudOrderPayload extends MinimalPayload {
    private Integer riskScore;
    private String riskDetails;

    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public String getRiskDetails() { return riskDetails; }
    public void setRiskDetails(String riskDetails) { this.riskDetails = riskDetails; }
}
