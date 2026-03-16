package com.homebase.ecom.order.port;

import java.math.BigDecimal;

/**
 * Domain port for payment operations.
 * Infrastructure layer provides the adapter (calls payment BC).
 */
public interface PaymentPort {
    /**
     * Request a refund for an order.
     */
    void requestRefund(String orderId, BigDecimal amount, String currency);

    /**
     * Check payment status for an order.
     */
    String getPaymentStatus(String orderId);
}
