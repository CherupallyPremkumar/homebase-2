package com.homebase.ecom.catalog.infrastructure.integration;

import com.homebase.ecom.catalog.port.client.OfferDataPort;
import com.homebase.ecom.catalog.port.client.OfferSnapshot;
import com.homebase.ecom.offer.api.OfferService;
import com.homebase.ecom.offer.api.dto.OfferDTO;

import java.util.Optional;

/**
 * Driven adapter: translates Offer bounded context → catalog's OfferSnapshot (ACL).
 * Uses offer-client proxy (via Chenile ProxyBuilder).
 */
public class OfferServiceAdapter implements OfferDataPort {

    private final OfferService offerService;

    public OfferServiceAdapter(OfferService offerService) {
        this.offerService = offerService;
    }

    @Override
    public Optional<OfferSnapshot> getOffer(String offerId) {
        try {
            OfferDTO offer = offerService.getOffer(offerId);
            if (offer == null) {
                return Optional.empty();
            }
            return Optional.of(toSnapshot(offer));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private OfferSnapshot toSnapshot(OfferDTO offer) {
        OfferSnapshot snapshot = new OfferSnapshot();
        snapshot.setId(offer.getId());
        snapshot.setProductId(offer.getProductId());
        snapshot.setSellerId(offer.getSupplierId());
        snapshot.setOfferPrice(offer.getOfferPrice());
        snapshot.setStatus(offer.getStatus());
        return snapshot;
    }
}
