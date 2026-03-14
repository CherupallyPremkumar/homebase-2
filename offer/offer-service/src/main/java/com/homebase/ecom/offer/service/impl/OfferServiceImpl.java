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
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public OfferDTO updateOffer(String id, OfferDTO offerDTO) {
        Offer existing = offerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Offer not found: " + id));
        existing.setPrice(offerDTO.getPrice());
        existing.setMsrp(offerDTO.getMsrp());
        offerRepository.save(existing);
        return toDTO(existing);
    }

    @Override
    public OfferDTO getOffer(String id) {
        var response = stateEntityService.retrieve(id);
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public void deleteOffer(String id) {
        offerRepository.delete(id);
    }

    @Override
    public OfferDTO submitForReview(String id) {
        var response = stateEntityService.processById(id, "submitForReview", null);
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public OfferDTO approveOffer(String id) {
        var response = stateEntityService.processById(id, "approveOffer", null);
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public OfferDTO rejectOffer(String id, String reason) {
        var response = stateEntityService.processById(id, "rejectOffer", reason);
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public OfferDTO deactivateOffer(String id) {
        var response = stateEntityService.processById(id, "deactivate", null);
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public OfferDTO activateOffer(String id) {
        var response = stateEntityService.processById(id, "activate", null);
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public OfferDTO returnOffer(String id, String reason) {
        var response = stateEntityService.processById(id, "returnOffer", reason);
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public OfferDTO resubmitOffer(String id) {
        var response = stateEntityService.processById(id, "resubmit", null);
        return toDTO(response.getMutatedEntity());
    }

    @Override
    public SearchResponse searchOffers(OfferSearchRequest searchRequest) {
        return null; // Search via QueryController
    }

    private OfferDTO toDTO(Offer offer) {
        if (offer == null) return null;
        OfferDTO dto = new OfferDTO();
        dto.setId(offer.getId());
        dto.setVariantId(offer.getVariantId());
        dto.setSupplierId(offer.getSupplierId());
        dto.setPrice(offer.getPrice());
        dto.setMsrp(offer.getMsrp());
        dto.setStatus(offer.getCurrentState() != null ? offer.getCurrentState().getStateId() : null);
        return dto;
    }
}
