package com.homebase.ecom.shared.event;

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
    public static final String CART_EVENTS = "cart.events";
    public static final String SUPPLIER_EVENTS = "supplier.events";
    public static final String SETTLEMENT_EVENTS = "settlement.events";
    public static final String SHIPPING_EVENTS = "shipping.events";
}
