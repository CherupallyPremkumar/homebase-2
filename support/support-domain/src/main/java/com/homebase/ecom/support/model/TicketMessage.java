package com.homebase.ecom.support.model;

public class TicketMessage {

    private String id;
    private String senderId;
    private String senderType; // CUSTOMER, AGENT, SYSTEM
    private String message;
    private String attachments; // JSON string

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
}
