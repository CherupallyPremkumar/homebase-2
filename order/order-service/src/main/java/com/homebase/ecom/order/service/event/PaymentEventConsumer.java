package com.homebase.ecom.order.service.event;

/**
 * DEPRECATED: Payment event consumption is now handled by {@link OrderEventConsumer#onPaymentEvent}.
 * This class is kept as an empty placeholder to avoid breaking any Spring wiring references.
 * The duplicate @KafkaListener has been removed to prevent double-processing of payment events.
 */
public class PaymentEventConsumer {
    // Intentionally empty - all payment event handling is consolidated in OrderEventConsumer
}
