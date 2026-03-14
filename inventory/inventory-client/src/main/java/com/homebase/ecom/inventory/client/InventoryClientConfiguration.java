package com.homebase.ecom.inventory.client;

import com.homebase.ecom.inventory.service.InventoryService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryClientConfiguration {
    @Bean
    public InventoryService inventoryServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            InventoryService.class,
            "_inventoryStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
