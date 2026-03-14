package com.homebase.ecom.offer.infrastructure.persistence.adapter;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.OfferRepository;
import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferEntity;
import com.homebase.ecom.offer.infrastructure.persistence.mapper.OfferMapper;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class OfferRepositoryImpl implements OfferRepository {

    private final OfferJpaRepository offerJpaRepository;
    private final OfferMapper offerMapper;

    public OfferRepositoryImpl(OfferJpaRepository offerJpaRepository, OfferMapper offerMapper) {
        this.offerJpaRepository = offerJpaRepository;
        this.offerMapper = offerMapper;
    }

    @Override
    public void save(Offer offer) {
        OfferEntity entity = offerMapper.toEntity(offer);
        offerJpaRepository.save(entity);
    }

    @Override
    public Optional<Offer> findById(String id) {
        return offerJpaRepository.findById(id).map(offerMapper::toModel);
    }

    @Override
    public void delete(String id) {
        offerJpaRepository.deleteById(id);
    }

    @Override
    public List<Offer> findExpiredTrials(Date now) {
        // TODO: implement query for expired trial offers
        return Collections.emptyList();
    }
}
