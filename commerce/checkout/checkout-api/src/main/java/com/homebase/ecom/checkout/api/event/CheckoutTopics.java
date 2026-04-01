package com.homebase.ecom.checkout.api.event;

/**
 * Published contract: Kafka topic for checkout events.
 * Consumers depend on checkout-api and use this constant.
 */
public final class CheckoutTopics {
    private CheckoutTopics() {}

    public static final String EVENTS = "checkout.events";
}
