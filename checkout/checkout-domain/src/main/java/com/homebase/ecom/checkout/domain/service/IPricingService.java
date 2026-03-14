package com.homebase.ecom.checkout.domain.service;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import com.homebase.ecom.checkout.domain.model.PriceSnapshot;
import java.util.UUID;

public interface IPricingService {
    PriceSnapshot lockPrice(CartSnapshot cart, UUID userId, String couponCode);
    void releaseLock(String lockToken);
}
