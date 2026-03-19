package com.homebase.ecom.catalog.port.client;

import java.util.Optional;

/**
 * Driven port — catalog asks product bounded context for data.
 * Adapter in infrastructure translates Product → ProductSnapshot (ACL).
 */
public interface ProductDataPort {
    Optional<ProductSnapshot> getProduct(String productId);
}
