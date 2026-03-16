package com.homebase.ecom.product.client;

import com.homebase.ecom.product.api.ProductService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.chenile.query.service.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductClientConfiguration {
    @Bean
    public ProductService productServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            ProductService.class,
            "_productStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }

    @Bean
    public SearchService productSearchServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            SearchService.class,
            "searchService",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
