package com.homebase.ecom.payment.gateway;

import java.math.BigDecimal;
import java.util.Map;

public class GatewayEvent {
    private String eventId;
    private String eventType; // e.g. PAYMENT_SUCCESS, REFUND_SUCCESS
    private String gatewayTransactionId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private Map<String, String> metadata;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
