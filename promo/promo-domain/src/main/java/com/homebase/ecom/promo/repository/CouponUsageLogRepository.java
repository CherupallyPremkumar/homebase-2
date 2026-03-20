package com.homebase.ecom.promo.repository;

import com.homebase.ecom.promo.model.CouponUsageLog;
import java.util.List;

public interface CouponUsageLogRepository {
    List<CouponUsageLog> findByCouponCodeAndUserId(String code, String userId);
    CouponUsageLog save(CouponUsageLog log);
}
