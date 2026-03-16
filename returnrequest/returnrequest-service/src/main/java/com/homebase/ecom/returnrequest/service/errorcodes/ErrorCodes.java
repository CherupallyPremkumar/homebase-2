package com.homebase.ecom.returnrequest.service.errorcodes;

public enum ErrorCodes {
    RETURN_WINDOW_EXPIRED(50001),
    INVALID_RETURN_REASON(50002),
    COMMENT_REQUIRED_ON_REJECT(50003),
    INVALID_REFUND_AMOUNT(50004),
    RETURN_REQUEST_NOT_FOUND(50005),
    MAX_ITEMS_EXCEEDED(50006),
    INSPECTION_REQUIRED(50007),
    DUPLICATE_RETURN_REQUEST(50008);

    private final int subError;

    private ErrorCodes(int subError) {
        this.subError = subError;
    }

    public int getSubError() {
        return this.subError;
    }
}
