package com.homebase.ecom.support.model;

import java.util.Date;
import java.util.List;

/**
 * Value object for a message in a support ticket conversation.
 */
public class TicketMessage {

    private String id;
    private String senderId;
    private String senderType; // CUSTOMER, AGENT, SYSTEM
    private String message;
    private Date timestamp;
    private List<String> attachments;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public List<String> getAttachments() { return attachments; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }
}
