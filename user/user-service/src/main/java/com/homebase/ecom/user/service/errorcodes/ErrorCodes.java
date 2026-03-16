package com.homebase.ecom.user.service.errorcodes;

/**
 * User module error codes.
 * Range: 1500-1599
 */
public enum ErrorCodes {
    USER_NOT_FOUND(1500),
    EMAIL_NOT_VERIFIED(1501),
    ACCOUNT_LOCKED(1502),
    ACCOUNT_SUSPENDED(1503),
    MAX_ADDRESSES_EXCEEDED(1504),
    ADDRESS_NOT_FOUND(1505),
    INVALID_PASSWORD(1506),
    DUPLICATE_EMAIL(1507),
    ACCOUNT_DEACTIVATED(1508),
    INVALID_VERIFICATION_TOKEN(1509),
    INVALID_EMAIL_FORMAT(1510),
    KYC_REQUIRED(1511),
    KYC_ALREADY_VERIFIED(1512),
    MAX_LOGIN_ATTEMPTS_EXCEEDED(1513);

    private final int subError;

    private ErrorCodes(int subError) {
        this.subError = subError;
    }

    public int getSubError() {
        return this.subError;
    }
}
