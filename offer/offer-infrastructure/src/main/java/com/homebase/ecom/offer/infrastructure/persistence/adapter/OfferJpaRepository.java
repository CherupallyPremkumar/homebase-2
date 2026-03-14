package com.homebase.ecom.offer.infrastructure.persistence.adapter;

import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferJpaRepository extends JpaRepository<OfferEntity, String> {
}
