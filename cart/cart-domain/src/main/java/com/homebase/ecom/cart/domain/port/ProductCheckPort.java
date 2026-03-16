package com.homebase.ecom.cart.domain.port;

/**
 * Domain port for verifying variant/product existence.
 * Infrastructure adapter calls the product search service (CQRS query).
 */
public interface ProductCheckPort {

    /**
     * Returns true if the variant exists under the given product
     * and the product is in a sellable state (PUBLISHED or ACTIVE).
     */
    boolean variantExists(String productId, String variantId);
}
