package com.homebase.ecom.promo.service;

import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.model.CouponUsageLog;
import com.homebase.ecom.promo.repository.CouponRepository;
import com.homebase.ecom.promo.repository.CouponUsageLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CouponUsageTracker {

    private static final Logger log = LoggerFactory.getLogger(CouponUsageTracker.class);

    private final CouponRepository couponRepository;
    private final CouponUsageLogRepository usageLogRepository;

    public CouponUsageTracker(CouponRepository couponRepository, CouponUsageLogRepository usageLogRepository) {
        this.couponRepository = couponRepository;
        this.usageLogRepository = usageLogRepository;
    }

    public boolean canApply(String code, String userId) {
        Optional<Coupon> couponOpt = couponRepository.findByCode(code);
        if (couponOpt.isEmpty()) return false;

        Coupon coupon = couponOpt.get();
        if (!coupon.isValid()) return false;

        if (coupon.isSingleUsePerCustomer()) {
            List<CouponUsageLog> usage = usageLogRepository.findByCouponCodeAndUserId(code, userId);
            if (!usage.isEmpty()) {
                log.info("Coupon {} already used by user {}", code, userId);
                return false;
            }
        }

        return true;
    }

    public void trackUsage(String code, String userId, String orderId) {
        Optional<Coupon> couponOpt = couponRepository.findByCode(code);
        if (couponOpt.isPresent()) {
            Coupon coupon = couponOpt.get();
            coupon.incrementUsage();
            couponRepository.save(coupon);

            CouponUsageLog usageLog = new CouponUsageLog();
            usageLog.setUsageId(UUID.randomUUID().toString());
            usageLog.setCouponCode(code);
            usageLog.setUserId(userId);
            usageLog.setOrderId(orderId);
            usageLog.setUsedAt(LocalDateTime.now());
            usageLogRepository.save(usageLog);
        }
    }
}
