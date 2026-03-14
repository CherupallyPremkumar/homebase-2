package com.homebase.ecom.offer.infrastructure.persistence;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.OfferRepository;
import org.chenile.utils.entity.service.EntityStore;

public class ChenileOfferEntityStore implements EntityStore<Offer> {

    private final OfferRepository offerRepository;

    public ChenileOfferEntityStore(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public void store(Offer offer) {
        offerRepository.save(offer);
    }

    @Override
    public Offer retrieve(String id) {
        return offerRepository.findById(id).orElse(null);
    }
}
