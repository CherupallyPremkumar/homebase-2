package com.homebase.ecom.support.service.errorcodes;

public enum ErrorCodes {
    TICKET_NOT_FOUND(80000),
    INVALID_TICKET_STATE(80001),
    AGENT_NOT_AVAILABLE(80002),
    EMPTY_REPLY_MESSAGE(80003),
    ESCALATION_REASON_REQUIRED(80004),
    RESOLUTION_REQUIRED(80005),
    REOPEN_REASON_REQUIRED(80006),
    TICKET_NOT_RESOLVED(80007);

    private final int subError;
    private ErrorCodes(int subError) {
        this.subError = subError;
    }

    public int getSubError() {
        return this.subError;
    }
}
