package com.homebase.ecom.returnprocessing.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingSagaRepository;
import com.homebase.ecom.shared.event.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

/**
 * Kafka listener that triggers the return processing saga when a return request
 * is approved (RETURN_APPROVED event on the RETURN_EVENTS topic).
 * Creates a new ReturnProcessingSaga in INITIATED state.
 */
public class ReturnApprovedEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReturnApprovedEventConsumer.class);
    private static final String RETURN_EVENTS_TOPIC = "return.events";
    private static final String RETURN_APPROVED_EVENT_TYPE = "RETURN_APPROVED";

    private final ReturnProcessingSagaRepository sagaRepository;
    private final ObjectMapper objectMapper;

    public ReturnApprovedEventConsumer(ReturnProcessingSagaRepository sagaRepository,
                                       ObjectMapper objectMapper) {
        this.sagaRepository = sagaRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = RETURN_EVENTS_TOPIC, groupId = "return-processing-service")
    public void onReturnEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if (!RETURN_APPROVED_EVENT_TYPE.equals(envelope.getEventType())) {
            log.debug("Ignoring event type: {}", envelope.getEventType());
            return;
        }

        log.info("Received RETURN_APPROVED event, initiating return processing saga");

        String returnRequestId = null;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = objectMapper.convertValue(envelope.getPayload(), Map.class);
            returnRequestId = (String) payload.get("returnRequestId");
            String orderId = (String) payload.get("orderId");
            String orderItemId = (String) payload.get("orderItemId");

            // Create a new saga entity in INITIATED state
            ReturnProcessingSaga saga = new ReturnProcessingSaga();
            saga.setReturnRequestId(returnRequestId);
            saga.setOrderId(orderId);
            saga.setOrderItemId(orderItemId);

            sagaRepository.save(saga);
            log.info("Return processing saga created for return request: {}", returnRequestId);
        } catch (DataIntegrityViolationException e) {
            log.warn("Idempotency: return processing saga for request {} already exists (possible replay). Skipping. Detail: {}",
                    returnRequestId, e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process RETURN_APPROVED event", e);
        }
    }
}
