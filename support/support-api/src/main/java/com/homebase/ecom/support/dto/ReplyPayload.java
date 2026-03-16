package com.homebase.ecom.support.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.util.List;

public class ReplyPayload extends MinimalPayload {
    private String message;
    private String senderType; // CUSTOMER, AGENT, SYSTEM
    private List<String> attachments;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }

    public List<String> getAttachments() { return attachments; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }
}
