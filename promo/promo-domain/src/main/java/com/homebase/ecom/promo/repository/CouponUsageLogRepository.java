package com.homebase.ecom.promo.repository;

import com.homebase.ecom.promo.model.CouponUsageLog;
import java.util.List;
import java.util.UUID;

public interface CouponUsageLogRepository {
    List<CouponUsageLog> findByCouponCodeAndUserId(String code, UUID userId);
    CouponUsageLog save(CouponUsageLog log);
}
