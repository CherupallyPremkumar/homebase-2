package com.homebase.ecom.order.infrastructure.integration;

import com.homebase.ecom.order.port.FulfillmentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for fulfillment operations.
 *
 * Currently a logging stub — will publish Kafka events to the fulfillment BC
 * once cross-service event wiring is complete.
 */
public class OrderFulfillmentAdapter implements FulfillmentPort {

    private static final Logger log = LoggerFactory.getLogger(OrderFulfillmentAdapter.class);

    @Override
    public void triggerFulfillment(String orderId) {
        log.info("Triggering fulfillment for orderId={}", orderId);
    }

    @Override
    public void cancelFulfillment(String orderId) {
        log.info("Cancelling fulfillment for orderId={}", orderId);
    }
}
