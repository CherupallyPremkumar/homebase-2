package com.homebase.ecom.promo.client;

import com.homebase.ecom.promo.service.PromotionService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromoClientConfiguration {
    @Bean
    public PromotionService promotionServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            PromotionService.class,
            "_promoStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
