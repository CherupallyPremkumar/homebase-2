package com.homebase.ecom.supplier.infrastructure.messaging;

import com.homebase.ecom.shared.event.SupplierApprovedEvent;
import com.homebase.ecom.shared.event.SupplierSuspendedEvent;
import com.homebase.ecom.shared.event.SupplierTerminatedEvent;
import com.homebase.ecom.supplier.domain.port.SupplierEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Infrastructure adapter for publishing supplier events.
 * Uses Spring ApplicationEventPublisher for in-process event dispatch.
 * Can be extended to publish to Kafka via ChenilePub to supplier.events topic.
 */
public class SupplierEventPublisherImpl implements SupplierEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SupplierEventPublisherImpl.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public SupplierEventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishSupplierApproved(SupplierApprovedEvent event) {
        log.info("Publishing SUPPLIER_APPROVED event for supplier: {}", event.getSupplierId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSupplierSuspended(SupplierSuspendedEvent event) {
        log.info("Publishing SUPPLIER_SUSPENDED event for supplier: {}", event.getSupplierId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSupplierTerminated(SupplierTerminatedEvent event) {
        log.info("Publishing SUPPLIER_TERMINATED event for supplier: {}", event.getSupplierId());
        applicationEventPublisher.publishEvent(event);
    }
}
