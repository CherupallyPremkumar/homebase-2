package com.homebase.ecom.offer.api;

import com.homebase.ecom.offer.api.dto.OfferDTO;
import com.homebase.ecom.offer.api.dto.OfferSearchRequest;
import org.chenile.query.model.SearchResponse;
import java.util.Map;

public interface OfferService {
    OfferDTO createOffer(OfferDTO offerDTO);
    OfferDTO updateOffer(String id, OfferDTO offerDTO);
    OfferDTO getOffer(String id);
    void deleteOffer(String id);
    
    // STM transitions
    OfferDTO submitForReview(String id);
    OfferDTO approveOffer(String id);
    OfferDTO rejectOffer(String id, String reason);
    OfferDTO deactivateOffer(String id);
    OfferDTO activateOffer(String id);
    OfferDTO returnOffer(String id, String reason);
    OfferDTO resubmitOffer(String id);

    // Search
    SearchResponse searchOffers(OfferSearchRequest searchRequest);
}
