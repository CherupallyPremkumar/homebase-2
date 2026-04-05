package com.homebase.ecom.promo.repository.jpa;

import com.homebase.ecom.promo.model.Campaign;
import com.homebase.ecom.promo.repository.CampaignRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignJpaRepository extends JpaRepository<Campaign, String>, CampaignRepository {
}
