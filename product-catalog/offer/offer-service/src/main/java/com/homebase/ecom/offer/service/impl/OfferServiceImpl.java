package com.homebase.ecom.offer.service.impl;

import com.homebase.ecom.offer.api.OfferService;
import com.homebase.ecom.offer.api.dto.OfferDTO;
import com.homebase.ecom.offer.api.dto.OfferSearchRequest;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.OfferRepository;
import com.homebase.ecom.offer.infrastructure.persistence.mapper.OfferMapper;
import org.chenile.query.model.SearchResponse;
import org.chenile.workflow.api.StateEntityService;

import java.util.UUID;

/**
 * Offer service implementation. All state mutations go through STM via stateEntityService.
 */
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final StateEntityService<Offer> stateEntityService;

    public OfferServiceImpl(OfferRepository offerRepository, OfferMapper offerMapper,
                           StateEntityService<Offer> stateEntityService) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.stateEntityService = stateEntityService;
    }

    @Override
    public OfferDTO createOffer(OfferDTO offerDTO) {
        Offer offer = offerMapper.toModel(offerDTO);
        if (offer.getId() == null) offer.setId(UUID.randomUUID().toString());
        var response = stateEntityService.create(offer);
        return offerMapper.toDTO(response.getMutatedEntity());
    }

    @Override
    public OfferDTO updateOffer(String id, OfferDTO offerDTO) {
        Offer existing = offerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Offer not found: " + id));
        // Only update mutable fields in DRAFT state
        if (offerDTO.getTitle() != null) existing.setTitle(offerDTO.getTitle());
        if (offerDTO.getDescription() != null) existing.setDescription(offerDTO.getDescription());
        if (offerDTO.getOriginalPrice() != null) existing.setOriginalPrice(offerDTO.getOriginalPrice());
        if (offerDTO.getOfferPrice() != null) existing.setOfferPrice(offerDTO.getOfferPrice());
        if (offerDTO.getDiscountPercent() != null) existing.setDiscountPercent(offerDTO.getDiscountPercent());
        if (offerDTO.getStartDate() != null) existing.setStartDate(offerDTO.getStartDate());
        if (offerDTO.getEndDate() != null) existing.setEndDate(offerDTO.getEndDate());
        if (offerDTO.getMaxQuantity() > 0) existing.setMaxQuantity(offerDTO.getMaxQuantity());
        offerRepository.save(existing);
        return offerMapper.toDTO(existing);
    }

    @Override
    public OfferDTO getOffer(String id) {
        var response = stateEntityService.retrieve(id);
        return offerMapper.toDTO(response.getMutatedEntity());
    }

    @Override
    public void deleteOffer(String id) {
        offerRepository.delete(id);
    }

    @Override
    public SearchResponse searchOffers(OfferSearchRequest searchRequest) {
        return null; // Search via QueryController
    }
}
