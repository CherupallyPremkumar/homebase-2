package com.homebase.ecom.support.dto;

import org.chenile.workflow.param.MinimalPayload;

public class ReplyPayload extends MinimalPayload {
    private String message;
    private String senderType; // CUSTOMER, AGENT, SYSTEM

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
}
