package com.homebase.ecom.offer.api;

import com.homebase.ecom.offer.api.dto.OfferDTO;
import com.homebase.ecom.offer.api.dto.OfferSearchRequest;
import org.chenile.query.model.SearchResponse;

public interface OfferService {
    OfferDTO createOffer(OfferDTO offerDTO);
    OfferDTO updateOffer(String id, OfferDTO offerDTO);
    OfferDTO getOffer(String id);
    void deleteOffer(String id);

    // Search
    SearchResponse searchOffers(OfferSearchRequest searchRequest);
}
