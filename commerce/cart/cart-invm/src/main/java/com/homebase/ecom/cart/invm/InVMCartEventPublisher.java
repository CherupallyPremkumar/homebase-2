package com.homebase.ecom.cart.invm;

import com.homebase.ecom.cart.event.CartEvent;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import org.chenile.core.event.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * In-VM event publisher for cart events — dumb pipe.
 * Serializes the domain event to JSON and routes via Chenile's EventProcessor.
 * No business logic, no event type mapping.
 */
public class InVMCartEventPublisher implements CartEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(InVMCartEventPublisher.class);
    private static final String TOPIC = "cart.events";

    private final EventProcessor eventProcessor;
    private final ObjectMapper objectMapper;

    public InVMCartEventPublisher(EventProcessor eventProcessor, ObjectMapper objectMapper) {
        this.eventProcessor = eventProcessor;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(CartEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            log.info("Publishing {} event for cartId={}", event.getEventType(), event.getCartId());
            eventProcessor.handleEvent(TOPIC, json);
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for cartId={}", event.getEventType(), event.getCartId(), e);
        }
    }
}
