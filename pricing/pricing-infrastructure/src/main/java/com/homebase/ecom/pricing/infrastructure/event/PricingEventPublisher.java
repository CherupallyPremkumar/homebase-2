package com.homebase.ecom.pricing.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PricingEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PricingEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String PRICING_TOPIC = "pricing-events";

    public PricingEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPriceLocked(PriceLockedEvent event) {
        log.info("Publishing PriceLockedEvent for order: {}", event.getOrderId());
        kafkaTemplate.send(PRICING_TOPIC, event.getOrderId(), event);
    }
}
