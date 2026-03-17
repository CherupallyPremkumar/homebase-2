package com.homebase.ecom.checkout.domain.port;

import com.homebase.ecom.checkout.model.CheckoutItem;
import java.util.List;

/**
 * Port for locking prices during checkout.
 * Adapter calls Pricing service's lockPrice endpoint.
 */
public interface PriceLockPort {

    /**
     * Locks prices for the given items and returns the locked price breakdown.
     */
    LockedPrice lockPrice(String cartId, String customerId, String currency,
                          List<CheckoutItem> items, List<String> couponCodes);

    record LockedPrice(
        long subtotal,
        long discountAmount,
        long taxAmount,
        long shippingCost,
        long finalTotal,
        String lockToken,
        String breakdownHash,
        String currency
    ) {}
}
