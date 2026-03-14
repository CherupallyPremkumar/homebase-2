package com.homebase.ecom.promo.repository.jpa;

import com.homebase.ecom.promo.model.Promotion;
import com.homebase.ecom.promo.repository.PromotionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionJpaRepository extends JpaRepository<Promotion, UUID>, PromotionRepository {
    
    @Override
    @Query("SELECT p FROM Promotion p WHERE p.active = true")
    List<Promotion> findAllActive();
}
