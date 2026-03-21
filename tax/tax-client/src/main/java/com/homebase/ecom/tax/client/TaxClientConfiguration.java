package com.homebase.ecom.tax.client;

import com.homebase.ecom.tax.service.TaxService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaxClientConfiguration {
    @Bean
    public TaxService taxServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            TaxService.class,
            "taxService",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
