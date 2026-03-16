package com.homebase.ecom.shipping.domain.port;

import com.homebase.ecom.shipping.model.Shipping;

import java.util.List;
import java.util.Optional;

/**
 * Port for shipment persistence.
 * Adapter: ShippingRepositoryImpl (infrastructure layer).
 */
public interface ShippingRepository {
    Optional<Shipping> findById(String id);

    Optional<Shipping> findByOrderId(String orderId);

    void save(Shipping shipping);

    void delete(String id);
}
