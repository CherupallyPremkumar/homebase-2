package com.homebase.ecom.returnrequest.infrastructure.adapter;

import com.homebase.ecom.returnrequest.domain.port.RefundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Adapter for the RefundPort. In production, this publishes a Kafka event
 * to the payment service to initiate the refund.
 */
public class RefundAdapter implements RefundPort {

    private static final Logger log = LoggerFactory.getLogger(RefundAdapter.class);

    @Override
    public void initiateRefund(String returnRequestId, String orderId, String customerId,
                               BigDecimal amount, String returnType) {
        log.info("Initiating refund for return request {}: orderId={}, customerId={}, amount={}, type={}",
                returnRequestId, orderId, customerId, amount, returnType);
        // In production: publish RefundInitiatedEvent to payment.events topic
    }
}
