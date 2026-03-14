package com.homebase.ecom.pricing.domain.service;

import com.homebase.ecom.shared.Money;
import com.homebase.ecom.pricing.domain.model.PriceBreakdown;
import com.homebase.ecom.pricing.domain.model.LockedPriceBreakdown;
import java.util.UUID;

public interface IPriceCalculator {
    PriceBreakdown calculatePrice(Object cartSnapshot, String couponCode);
    LockedPriceBreakdown lockPrice(UUID orderId, Object cartSnapshot, String couponCode);
}
