package com.homebase.ecom.supplier.infrastructure.messaging;

import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.SupplierApprovedEvent;
import com.homebase.ecom.shared.event.SupplierSuspendedEvent;
import com.homebase.ecom.shared.event.SupplierTerminatedEvent;
import com.homebase.ecom.supplier.domain.port.SupplierEventPublisher;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Infrastructure adapter for publishing supplier lifecycle events to Kafka.
 * Uses Chenile's ChenilePub for async publish-after-commit to supplier.events topic.
 * Events are consumed by: onboarding (APPROVED), supplier-lifecycle saga (SUSPENDED),
 * product BC (TERMINATED -> disable products).
 */
public class SupplierEventPublisherImpl implements SupplierEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SupplierEventPublisherImpl.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public SupplierEventPublisherImpl(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishSupplierApproved(SupplierApprovedEvent event) {
        log.info("Publishing SUPPLIER_APPROVED event for supplier: {}", event.getSupplierId());
        publishAfterCommit(event.getSupplierId(), event, SupplierApprovedEvent.EVENT_TYPE);
    }

    @Override
    public void publishSupplierSuspended(SupplierSuspendedEvent event) {
        log.info("Publishing SUPPLIER_SUSPENDED event for supplier: {}", event.getSupplierId());
        publishAfterCommit(event.getSupplierId(), event, SupplierSuspendedEvent.EVENT_TYPE);
    }

    @Override
    public void publishSupplierTerminated(SupplierTerminatedEvent event) {
        log.info("Publishing SUPPLIER_TERMINATED event for supplier: {}", event.getSupplierId());
        publishAfterCommit(event.getSupplierId(), event, SupplierTerminatedEvent.EVENT_TYPE);
    }

    /**
     * Publishes event to Kafka after the current transaction commits.
     * If no transaction is active, publishes immediately.
     * Uses asyncPublish for non-blocking delivery.
     */
    private void publishAfterCommit(String supplierId, Object event, String eventType) {
        Runnable publishAction = () -> {
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.asyncPublish(KafkaTopics.SUPPLIER_EVENTS, body,
                        Map.of("key", supplierId, "eventType", eventType));
                log.info("Published {} event to {} for supplierId={}", eventType,
                        KafkaTopics.SUPPLIER_EVENTS, supplierId);
            } catch (JacksonException e) {
                log.error("Failed to serialize {} event for supplierId={}", eventType, supplierId, e);
            }
        };

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishAction.run();
                }
            });
        } else {
            publishAction.run();
        }
    }
}
