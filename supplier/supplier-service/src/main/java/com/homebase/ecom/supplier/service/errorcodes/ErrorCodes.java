package com.homebase.ecom.supplier.service.errorcodes;

public enum ErrorCodes {
    SUPPLIER_NOT_FOUND(50000),
    REJECTION_REASON_REQUIRED(50001),
    SUSPENSION_REASON_REQUIRED(50002),
    INCOMPLETE_SUPPLIER_PROFILE(50003),
    BANK_ACCOUNT_REQUIRED(50004),
    MAX_PRODUCTS_EXCEEDED(50005),
    INVALID_TRANSITION(50006),
    TERMINATION_REASON_REQUIRED(50007),
    BELOW_MIN_RATING(50008),
    BELOW_MIN_FULFILLMENT_RATE(50009),
    PROBATION_REASON_REQUIRED(50010);

    private final int subError;

    ErrorCodes(int subError) {
        this.subError = subError;
    }

    public int getSubError() {
        return this.subError;
    }
}
