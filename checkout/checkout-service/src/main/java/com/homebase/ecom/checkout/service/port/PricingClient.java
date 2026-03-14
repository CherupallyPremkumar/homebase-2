package com.homebase.ecom.checkout.service.port;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.model.PriceSnapshot;
import java.util.UUID;

public interface PricingClient {
    PriceSnapshot lockPrice(Checkout checkout, String couponCode);
}
