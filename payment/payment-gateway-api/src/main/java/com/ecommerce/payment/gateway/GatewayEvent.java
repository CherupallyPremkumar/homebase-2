package com.ecommerce.payment.gateway;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class GatewayEvent {
    private String eventId;
    private String eventType; // e.g. PAYMENT_SUCCESS, REFUND_SUCCESS
    private String gatewayTransactionId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private Map<String, String> metadata;
}
