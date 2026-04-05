package com.homebase.ecom.promo.port;

import java.util.UUID;

/**
 * Item 12: Hexagonal port for tracking promo usage per customer.
 * Abstracts the usage tracking mechanism from the domain.
 */
public interface UsageTrackingPort {

    /**
     * Returns the number of times a customer has used a specific promo code.
     */
    int getUsageCountForCustomer(String promoCode, String customerId);

    /**
     * Records a usage of a promo code by a customer for an order.
     */
    void recordUsage(String promoCode, String customerId, String orderId);

    /**
     * Checks if a customer can still use this promo code based on per-customer limits.
     */
    boolean canCustomerUse(String promoCode, String customerId, int maxUsagePerCustomer);
}
