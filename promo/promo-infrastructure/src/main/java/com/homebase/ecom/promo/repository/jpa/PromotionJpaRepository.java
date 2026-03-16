package com.homebase.ecom.promo.repository.jpa;

import com.homebase.ecom.promo.model.Promotion;
import com.homebase.ecom.promo.repository.PromotionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionJpaRepository extends JpaRepository<Promotion, String>, PromotionRepository {
    
    @Override
    @Query("SELECT p FROM Promotion p WHERE p.active = true")
    List<Promotion> findAllActive();
}
