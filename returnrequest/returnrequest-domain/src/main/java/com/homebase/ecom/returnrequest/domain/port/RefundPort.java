package com.homebase.ecom.returnrequest.domain.port;

import java.math.BigDecimal;

/**
 * Port for triggering payment refund processing.
 * Adapter connects to the payment service.
 */
public interface RefundPort {
    /**
     * Initiates a refund for the given return request.
     * @param returnRequestId the return request ID
     * @param orderId the original order ID
     * @param customerId the customer ID
     * @param amount the refund amount
     * @param returnType REFUND, EXCHANGE, or STORE_CREDIT
     */
    void initiateRefund(String returnRequestId, String orderId, String customerId,
                        BigDecimal amount, String returnType);
}
