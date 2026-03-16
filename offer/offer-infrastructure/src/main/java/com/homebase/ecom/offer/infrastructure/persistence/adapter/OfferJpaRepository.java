package com.homebase.ecom.offer.infrastructure.persistence.adapter;

import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferJpaRepository extends JpaRepository<OfferEntity, String> {

    List<OfferEntity> findByProductId(String productId);

    @Query("SELECT o FROM OfferEntity o WHERE o.productId = :productId AND o.currentState.stateId = 'LIVE'")
    List<OfferEntity> findLiveOffersByProductId(@Param("productId") String productId);

    @Query("SELECT COUNT(o) FROM OfferEntity o WHERE o.productId = :productId AND o.currentState.stateId IN ('DRAFT','PENDING_APPROVAL','APPROVED','LIVE')")
    int countActiveOffersByProductId(@Param("productId") String productId);
}
