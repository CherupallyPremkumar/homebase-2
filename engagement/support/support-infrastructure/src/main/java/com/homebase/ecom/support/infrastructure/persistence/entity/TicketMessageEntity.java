package com.homebase.ecom.support.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Entity
@Table(name = "ticket_messages")
public class TicketMessageEntity extends BaseJpaEntity {

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "sender_type", nullable = false)
    private String senderType;

    @Column(nullable = false, length = 4000)
    private String message;

    @Column(name = "message_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String attachments;

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
}
