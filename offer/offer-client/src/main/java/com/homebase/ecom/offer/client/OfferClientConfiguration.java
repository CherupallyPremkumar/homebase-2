package com.homebase.ecom.offer.client;

import com.homebase.ecom.offer.api.OfferService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferClientConfiguration {
    @Bean
    public OfferService offerServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            OfferService.class,
            "_offerStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
