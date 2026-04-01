package com.homebase.ecom.promo.repository;

import com.homebase.ecom.promo.model.Campaign;
import java.util.Optional;

/**
 * Repository interface for Campaign entity.
 * Uses String id (from BaseJpaEntity).
 */
public interface CampaignRepository {
    Optional<Campaign> findById(String id);
    Campaign save(Campaign campaign);
    void deleteById(String id);
}
