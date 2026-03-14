package com.homebase.ecom.promo.repository.jpa;

import com.homebase.ecom.promo.model.CouponUsageLog;
import com.homebase.ecom.promo.repository.CouponUsageLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CouponUsageLogJpaRepository extends JpaRepository<CouponUsageLog, UUID>, CouponUsageLogRepository {
    
    @Override
    List<CouponUsageLog> findByCouponCodeAndUserId(String couponCode, UUID userId);
}
