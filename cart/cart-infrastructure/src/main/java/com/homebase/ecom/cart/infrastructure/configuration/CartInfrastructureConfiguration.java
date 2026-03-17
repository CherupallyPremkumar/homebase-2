package com.homebase.ecom.cart.infrastructure.configuration;

import com.homebase.ecom.cart.domain.port.InventoryCheckPort;
import com.homebase.ecom.cart.domain.port.PricingPort;
import com.homebase.ecom.cart.domain.port.ProductCheckPort;
import com.homebase.ecom.cart.infrastructure.adapter.InventoryCheckAdapter;
import com.homebase.ecom.cart.infrastructure.adapter.PricingAdapter;
import com.homebase.ecom.cart.infrastructure.adapter.ProductCheckAdapter;
import com.homebase.ecom.pricing.api.service.PricingService;
import org.chenile.query.service.SearchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer: wires adapters to domain ports.
 *
 * Each adapter is a pure translator (external DTO ↔ domain model).
 * The proxy beans (PricingService, SearchService) come from client modules
 * via ProxyBuilder — they're lazy JDK proxies that route local/remote at invocation time.
 */
@Configuration
public class CartInfrastructureConfiguration {

    @Bean
    PricingPort pricingPort(PricingService pricingServiceClient) {
        return new PricingAdapter(pricingServiceClient);
    }

    @Bean
    InventoryCheckPort inventoryCheckPort(
            @Qualifier("inventorySearchServiceClient") SearchService inventorySearchService) {
        return new InventoryCheckAdapter(inventorySearchService);
    }

    @Bean
    ProductCheckPort productCheckPort(
            @Qualifier("productSearchServiceClient") SearchService productSearchService) {
        return new ProductCheckAdapter(productSearchService);
    }
}
