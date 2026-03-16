package com.homebase.ecom.catalog.client;

import com.homebase.ecom.catalog.service.CategoryService;
import com.homebase.ecom.catalog.service.CollectionService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogClientConfiguration {
    @Bean
    public CategoryService categoryServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            CategoryService.class,
            "categoryServiceImpl",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }

    @Bean
    public CollectionService collectionServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            CollectionService.class,
            "collectionServiceImpl",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
