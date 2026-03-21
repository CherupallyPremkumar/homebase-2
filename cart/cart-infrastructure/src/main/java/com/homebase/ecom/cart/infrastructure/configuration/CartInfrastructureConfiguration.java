package com.homebase.ecom.cart.infrastructure.configuration;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.repository.CartRepository;
import com.homebase.ecom.cart.port.ConfigPort;
import com.homebase.ecom.cart.port.InventoryCheckPort;
import com.homebase.ecom.cart.port.PricingPort;
import com.homebase.ecom.cart.port.ProductCheckPort;
import com.homebase.ecom.cart.infrastructure.integration.CconfigCartConfigAdapter;
import com.homebase.ecom.cart.infrastructure.persistence.adapter.InventoryCheckAdapter;
import com.homebase.ecom.cart.infrastructure.persistence.adapter.PricingAdapter;
import com.homebase.ecom.cart.infrastructure.persistence.adapter.ProductCheckAdapter;
import com.homebase.ecom.cart.infrastructure.persistence.store.ChenileCartEntityStore;
import com.homebase.ecom.cart.infrastructure.persistence.repository.CartJpaRepository;
import com.homebase.ecom.cart.infrastructure.persistence.mapper.CartMapper;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import com.homebase.ecom.pricing.api.service.PricingService;
import org.chenile.query.service.SearchService;
import org.chenile.utils.entity.service.EntityStore;
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
    EntityStore<Cart> cartEntityStore(CartJpaRepository repository, CartMapper mapper) {
        return new ChenileCartEntityStore(repository, mapper);
    }

    @Bean
    CartRepository cartRepository(@Qualifier("cartEntityStore") EntityStore<Cart> entityStore) {
        return (CartRepository) entityStore;
    }

    @Bean
    ConfigPort cartConfigPort(CconfigClient cconfigClient) {
        return new CconfigCartConfigAdapter(cconfigClient);
    }

    @Bean("cartPricingPort")
    PricingPort cartPricingPort(PricingService pricingServiceClient) {
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
