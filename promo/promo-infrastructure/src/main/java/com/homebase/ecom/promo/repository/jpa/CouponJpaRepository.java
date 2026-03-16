package com.homebase.ecom.promo.repository.jpa;

import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.repository.CouponRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponJpaRepository extends JpaRepository<Coupon, String>, CouponRepository {

    @Override
    Optional<Coupon> findByCode(String code);
}
