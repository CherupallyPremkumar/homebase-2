package com.homebase.ecom.promo.service.store;

import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.repository.CouponRepository;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Chenile EntityStore implementation for Coupon.
 * Uses String id from BaseJpaEntity.
 */
@Component
public class PromoEntityStore implements EntityStore<Coupon> {

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public void store(Coupon entity) {
        couponRepository.save(entity);
    }

    @Override
    public Coupon retrieve(String id) {
        return couponRepository.findById(id).orElse(null);
    }
}
