package com.homebase.ecom.promo.adapter;

import com.homebase.ecom.promo.model.CouponUsageLog;
import com.homebase.ecom.promo.port.UsageTrackingPort;
import com.homebase.ecom.promo.repository.CouponUsageLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Item 12: Infrastructure adapter implementing UsageTrackingPort.
 * Uses CouponUsageLogRepository for persistence.
 */
@Component
public class UsageTrackingAdapter implements UsageTrackingPort {

    private final CouponUsageLogRepository usageLogRepository;

    public UsageTrackingAdapter(CouponUsageLogRepository usageLogRepository) {
        this.usageLogRepository = usageLogRepository;
    }

    @Override
    public int getUsageCountForCustomer(String promoCode, String customerId) {
        List<CouponUsageLog> logs = usageLogRepository.findByCouponCodeAndUserId(
                promoCode, UUID.fromString(customerId));
        return logs != null ? logs.size() : 0;
    }

    @Override
    public void recordUsage(String promoCode, String customerId, String orderId) {
        CouponUsageLog log = new CouponUsageLog();
        log.setCouponCode(promoCode);
        log.setUserId(UUID.fromString(customerId));
        log.setOrderId(UUID.fromString(orderId));
        log.setUsedAt(LocalDateTime.now());
        usageLogRepository.save(log);
    }

    @Override
    public boolean canCustomerUse(String promoCode, String customerId, int maxUsagePerCustomer) {
        int currentUsage = getUsageCountForCustomer(promoCode, customerId);
        return currentUsage < maxUsagePerCustomer;
    }
}
