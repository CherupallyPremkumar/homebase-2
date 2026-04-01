package com.homebase.ecom.shipping.service.errorcodes;

public enum ErrorCodes {
    INVALID_SHIPPING_METHOD(17001),
    MAX_DELIVERY_ATTEMPTS_EXCEEDED(17002),
    SHIPMENT_NOT_FOUND(17003),
    INVALID_STATE_TRANSITION(17004);

    private final int subError;
    private ErrorCodes(int subError) {
        this.subError = subError;
    }

    public int getSubError() {
        return this.subError;
    }
}
