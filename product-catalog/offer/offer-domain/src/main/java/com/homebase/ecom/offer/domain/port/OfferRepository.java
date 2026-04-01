package com.homebase.ecom.offer.domain.port;

import com.homebase.ecom.offer.domain.model.Offer;
import java.util.List;
import java.util.Optional;

/**
 * Port for offer persistence. Implemented by infrastructure adapter.
 */
public interface OfferRepository {
    Optional<Offer> findById(String id);
    void save(Offer offer);
    void delete(String id);
    List<Offer> findByProductId(String productId);
    List<Offer> findLiveOffersByProductId(String productId);
    int countActiveOffersByProductId(String productId);
}
