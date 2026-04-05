package com.homebase.ecom.promo.repository;

import com.homebase.ecom.promo.model.Promotion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromotionRepository {
    Optional<Promotion> findById(String id);
    List<Promotion> findAllActive();
    Promotion save(Promotion promotion);
    void deleteById(String id);
}

