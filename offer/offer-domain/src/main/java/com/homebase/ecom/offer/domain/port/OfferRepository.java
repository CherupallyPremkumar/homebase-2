package com.homebase.ecom.offer.domain.port;

import com.homebase.ecom.offer.domain.model.Offer;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Optional<Offer> findById(String id);
    void save(Offer offer);
    void delete(String id);
    List<Offer> findExpiredTrials(Date now);
}
