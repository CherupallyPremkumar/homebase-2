package com.homebase.ecom.shared.model.event;

/**
 * Kafka topic name constants for cross-module communication.
 */
public final class KafkaTopics {

    private KafkaTopics() {
    }

    public static final String ORDER_EVENTS = "order.events";
    public static final String PRODUCT_EVENTS = "product.events";
    public static final String PAYMENT_EVENTS = "payment.events";
    public static final String INVENTORY_EVENTS = "inventory.events";
    public static final String AUDIT_EVENTS = "audit.events";
}
