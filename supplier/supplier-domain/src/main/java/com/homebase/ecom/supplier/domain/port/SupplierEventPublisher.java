package com.homebase.ecom.supplier.domain.port;

import com.homebase.ecom.shared.event.SupplierActivatedEvent;
import com.homebase.ecom.shared.event.SupplierSuspendedEvent;
import com.homebase.ecom.shared.event.SupplierBlacklistedEvent;

/**
 * Domain port for publishing supplier lifecycle events.
 * Infrastructure layer provides the actual Kafka/messaging implementation.
 */
public interface SupplierEventPublisher {

    void publishSupplierActivated(SupplierActivatedEvent event);

    void publishSupplierSuspended(SupplierSuspendedEvent event);

    void publishSupplierBlacklisted(SupplierBlacklistedEvent event);
}
