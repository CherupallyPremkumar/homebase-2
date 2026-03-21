package com.homebase.ecom.checkout.invm;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.event.CheckoutEvent;
import org.chenile.core.event.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * In-VM event publisher for checkout events — dumb pipe.
 * Serializes the domain event to JSON and routes via Chenile's EventProcessor.
 * No business logic, no event type mapping.
 */
public class InVMCheckoutEventPublisher implements CheckoutEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(InVMCheckoutEventPublisher.class);
    private static final String TOPIC = "checkout.events";

    private final EventProcessor eventProcessor;
    private final ObjectMapper objectMapper;

    public InVMCheckoutEventPublisher(EventProcessor eventProcessor, ObjectMapper objectMapper) {
        this.eventProcessor = eventProcessor;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(CheckoutEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            log.info("Publishing {} event for checkoutId={}", event.getEventType(), event.getCheckoutId());
            eventProcessor.handleEvent(TOPIC, json);
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for checkoutId={}", event.getEventType(), event.getCheckoutId(), e);
        }
    }
}
