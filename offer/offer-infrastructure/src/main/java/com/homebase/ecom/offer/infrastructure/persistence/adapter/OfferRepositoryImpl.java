package com.homebase.ecom.offer.infrastructure.persistence.adapter;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.OfferRepository;
import com.homebase.ecom.offer.infrastructure.persistence.mapper.OfferMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OfferRepositoryImpl implements OfferRepository {

    private final OfferJpaRepository offerJpaRepository;
    private final OfferMapper offerMapper;

    public OfferRepositoryImpl(OfferJpaRepository offerJpaRepository, OfferMapper offerMapper) {
        this.offerJpaRepository = offerJpaRepository;
        this.offerMapper = offerMapper;
    }

    @Override
    public void save(Offer offer) {
        offerJpaRepository.save(offerMapper.toEntity(offer));
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
    public List<Offer> findByProductId(String productId) {
        return offerJpaRepository.findByProductId(productId).stream()
                .map(offerMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Offer> findLiveOffersByProductId(String productId) {
        return offerJpaRepository.findLiveOffersByProductId(productId).stream()
                .map(offerMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public int countActiveOffersByProductId(String productId) {
        return offerJpaRepository.countActiveOffersByProductId(productId);
    }
}
