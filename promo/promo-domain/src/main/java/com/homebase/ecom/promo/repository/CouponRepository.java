package com.homebase.ecom.promo.repository;

import com.homebase.ecom.promo.model.Coupon;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {
    Optional<Coupon> findByCode(String code);
    Optional<Coupon> findById(UUID id);
    Coupon save(Coupon coupon);
    void deleteById(UUID id);
}
