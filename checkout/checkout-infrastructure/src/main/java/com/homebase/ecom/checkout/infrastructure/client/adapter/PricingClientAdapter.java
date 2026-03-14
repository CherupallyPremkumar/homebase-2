package com.homebase.ecom.checkout.infrastructure.client.adapter;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.model.PriceSnapshot;
import com.homebase.ecom.checkout.infrastructure.client.PricingServiceClient;
import com.homebase.ecom.checkout.service.port.PricingClient;
import org.springframework.stereotype.Component;

@Component
public class PricingClientAdapter implements PricingClient {
    private final PricingServiceClient pricingServiceClient;

    public PricingClientAdapter(PricingServiceClient pricingServiceClient) {
        this.pricingServiceClient = pricingServiceClient;
    }

    @Override
    public PriceSnapshot lockPrice(Checkout checkout, String couponCode) {
        return pricingServiceClient.lockPrice(checkout.getLockedCart(), checkout.getUserId(), couponCode);
    }
}
