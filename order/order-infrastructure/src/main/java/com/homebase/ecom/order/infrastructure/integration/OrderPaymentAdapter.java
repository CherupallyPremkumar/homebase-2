package com.homebase.ecom.order.infrastructure.integration;

import com.homebase.ecom.order.port.PaymentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Infrastructure adapter for payment operations.
 *
 * Currently a logging stub — will delegate to payment-client
 * once cross-service event wiring is complete.
 */
public class OrderPaymentAdapter implements PaymentPort {

    private static final Logger log = LoggerFactory.getLogger(OrderPaymentAdapter.class);

    @Override
    public void requestRefund(String orderId, BigDecimal amount, String currency) {
        log.info("Requesting refund for orderId={}, amount={} {}", orderId, amount, currency);
    }

    @Override
    public String getPaymentStatus(String orderId) {
        log.info("Checking payment status for orderId={}", orderId);
        return "UNKNOWN";
    }
}
