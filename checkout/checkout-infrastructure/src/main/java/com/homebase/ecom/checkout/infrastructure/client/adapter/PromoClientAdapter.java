package com.homebase.ecom.checkout.infrastructure.client.adapter;

import com.homebase.ecom.checkout.infrastructure.client.PromoServiceClient;
import com.homebase.ecom.checkout.service.port.PromoClient;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class PromoClientAdapter implements PromoClient {
    private final PromoServiceClient promoServiceClient;

    public PromoClientAdapter(PromoServiceClient promoServiceClient) {
        this.promoServiceClient = promoServiceClient;
    }

    @Override
    public void commitPromo(String couponCode, UUID userId) {
        promoServiceClient.commitPromo(couponCode, userId);
    }

    @Override
    public void releasePromo(String couponCode, UUID userId) {
        promoServiceClient.releasePromo(couponCode, userId);
    }
}
