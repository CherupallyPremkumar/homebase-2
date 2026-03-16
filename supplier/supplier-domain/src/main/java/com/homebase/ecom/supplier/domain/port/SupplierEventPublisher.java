package com.homebase.ecom.supplier.domain.port;

import com.homebase.ecom.shared.event.SupplierApprovedEvent;
import com.homebase.ecom.shared.event.SupplierSuspendedEvent;
import com.homebase.ecom.shared.event.SupplierTerminatedEvent;

/**
 * Domain port for publishing supplier lifecycle events to supplier.events topic.
 * Infrastructure layer provides the actual Kafka/messaging implementation.
 */
public interface SupplierEventPublisher {

    void publishSupplierApproved(SupplierApprovedEvent event);

    void publishSupplierSuspended(SupplierSuspendedEvent event);

    void publishSupplierTerminated(SupplierTerminatedEvent event);
}
