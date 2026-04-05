package com.homebase.ecom.catalog.client;

import com.homebase.ecom.catalog.dto.CategoryDTO;
import com.homebase.ecom.catalog.dto.CollectionDTO;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Client configuration for catalog service.
 * Uses DTOs from catalog-api — never imports domain models.
 * Other BCs use this client to query catalog data.
 */
@Configuration
public class CatalogClientConfiguration {

    @Bean
    public CatalogClient catalogClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            CatalogClient.class,
            "catalogServiceImpl",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
