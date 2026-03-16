package com.homebase.ecom.promo.port;

import com.homebase.ecom.promo.model.Coupon;
import java.util.List;
import java.util.Optional;

/**
 * Item 12: Hexagonal port for promo persistence.
 * Domain-level abstraction; infrastructure adapters implement this.
 * Uses String id (from BaseJpaEntity/IDGenerator).
 */
public interface PromoRepository {
    Optional<Coupon> findById(String id);
    Optional<Coupon> findByCode(String code);
    Coupon save(Coupon coupon);
    void deleteById(String id);
    List<Coupon> findByStateId(String stateId);
    long countByStateId(String stateId);
}
