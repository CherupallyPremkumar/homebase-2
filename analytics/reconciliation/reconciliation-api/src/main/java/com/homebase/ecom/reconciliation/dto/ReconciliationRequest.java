package com.homebase.ecom.reconciliation.dto;

import java.time.LocalDate;

public class ReconciliationRequest {
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String gatewayType;

    public ReconciliationRequest() {
    }

    public ReconciliationRequest(LocalDate periodStart, LocalDate periodEnd, String gatewayType) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.gatewayType = gatewayType;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }
}
