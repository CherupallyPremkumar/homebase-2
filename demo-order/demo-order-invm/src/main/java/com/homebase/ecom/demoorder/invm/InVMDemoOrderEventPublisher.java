package com.homebase.ecom.demoorder.invm;

import com.homebase.ecom.demoorder.event.DemoOrderEvent;
import com.homebase.ecom.demoorder.port.DemoOrderEventPublisherPort;
import org.chenile.core.event.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * In-VM event publisher for demo order events -- dumb pipe.
 * Serializes the domain event to JSON and routes via Chenile's EventProcessor.
 * No business logic, no event type mapping.
 */
public class InVMDemoOrderEventPublisher implements DemoOrderEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(InVMDemoOrderEventPublisher.class);
    private static final String TOPIC = "demo-order.events";

    private final EventProcessor eventProcessor;
    private final ObjectMapper objectMapper;

    public InVMDemoOrderEventPublisher(EventProcessor eventProcessor, ObjectMapper objectMapper) {
        this.eventProcessor = eventProcessor;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DemoOrderEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            log.info("Publishing {} event for orderId={}", event.getEventType(), event.getOrderId());
            eventProcessor.handleEvent(TOPIC, json);
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for orderId={}", event.getEventType(), event.getOrderId(), e);
        }
    }
}
