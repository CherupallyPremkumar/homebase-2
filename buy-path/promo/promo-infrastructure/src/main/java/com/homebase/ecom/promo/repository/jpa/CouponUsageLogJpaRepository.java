package com.homebase.ecom.promo.repository.jpa;

import com.homebase.ecom.promo.model.CouponUsageLog;
import com.homebase.ecom.promo.repository.CouponUsageLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponUsageLogJpaRepository extends JpaRepository<CouponUsageLog, String>, CouponUsageLogRepository {

    @Override
    List<CouponUsageLog> findByCouponCodeAndUserId(String couponCode, String userId);
}
