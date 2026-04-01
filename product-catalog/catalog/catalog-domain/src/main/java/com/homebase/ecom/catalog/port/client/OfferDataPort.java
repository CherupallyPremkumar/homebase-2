package com.homebase.ecom.catalog.port.client;

import java.util.Optional;

/**
 * Driven port — catalog asks offer bounded context for data.
 * Adapter in infrastructure translates Offer → OfferSnapshot (ACL).
 */
public interface OfferDataPort {
    Optional<OfferSnapshot> getOffer(String offerId);
}
