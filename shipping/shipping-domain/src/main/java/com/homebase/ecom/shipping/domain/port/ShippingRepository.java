package com.homebase.ecom.shipping.domain.port;

import com.homebase.ecom.shipping.model.Shipping;

import java.util.Optional;

public interface ShippingRepository {
    Optional<Shipping> findById(String id);

    void save(Shipping shipping);

    void delete(String id);
}
