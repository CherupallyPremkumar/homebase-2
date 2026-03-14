package com.homebase.ecom.settlement.model;

public enum SettlementStatus {
    PENDING,
    CALCULATED,
    PROCESSING,
    READY_FOR_PAYMENT,
    MANUAL_REVIEW,
    SETTLED,
    FAILED
}
