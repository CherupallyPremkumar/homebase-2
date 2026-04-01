package com.homebase.ecom.promo.repository;

import com.homebase.ecom.promo.model.Coupon;
import java.util.Optional;

/**
 * Repository interface for Coupon entity.
 * Uses String id (from BaseJpaEntity).
 */
public interface CouponRepository {
    Optional<Coupon> findByCode(String code);
    Optional<Coupon> findById(String id);
    Coupon save(Coupon coupon);
    void deleteById(String id);
}
