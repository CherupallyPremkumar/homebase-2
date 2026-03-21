package com.homebase.ecom.checkout.infrastructure.configuration;

import com.homebase.ecom.checkout.domain.port.InventoryReservePort;
import com.homebase.ecom.checkout.domain.port.OrderCreationPort;
import com.homebase.ecom.checkout.domain.port.PriceLockPort;
import com.homebase.ecom.checkout.domain.port.PromoCommitPort;
import com.homebase.ecom.checkout.domain.port.ShippingValidationPort;
import com.homebase.ecom.checkout.infrastructure.integration.CheckoutPriceLockAdapter;
import com.homebase.ecom.checkout.infrastructure.integration.InventoryReserveAdapter;
import com.homebase.ecom.checkout.infrastructure.integration.OrderCreationAdapter;
import com.homebase.ecom.checkout.infrastructure.integration.PromoCommitAdapter;
import com.homebase.ecom.checkout.infrastructure.integration.ShippingValidationAdapter;
import com.homebase.ecom.checkout.infrastructure.persistence.ChenileCheckoutEntityStore;
import com.homebase.ecom.checkout.infrastructure.persistence.mapper.CheckoutMapper;
import com.homebase.ecom.checkout.infrastructure.persistence.repository.CheckoutJpaRepository;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.inventory.service.InventoryService;
import com.homebase.ecom.order.service.OrderService;
import com.homebase.ecom.pricing.api.service.PricingService;
import com.homebase.ecom.promo.service.PromotionService;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.api.StateEntityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer: wires checkout adapters to domain ports,
 * plus EntityStore and Mapper (persistence belongs in infrastructure).
 *
 * Each adapter is a pure translator (external DTO <-> domain model).
 * The proxy beans come from client modules via ProxyBuilder --
 * they're lazy JDK proxies that route local/remote at invocation time.
 */
@Configuration
public class CheckoutInfrastructureConfiguration {

    // ═══════════════════════════════════════════════════════════════════
    // Persistence: Mapper + EntityStore
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    CheckoutMapper checkoutMapper() {
        return new CheckoutMapper();
    }

    @Bean
    EntityStore<Checkout> checkoutEntityStore(CheckoutJpaRepository repository, CheckoutMapper mapper) {
        return new ChenileCheckoutEntityStore(repository, mapper);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Cross-BC Port Adapters
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    InventoryReservePort checkoutInventoryReservePort(InventoryService inventoryServiceClient) {
        return new InventoryReserveAdapter(inventoryServiceClient);
    }

    @Bean
    OrderCreationPort checkoutOrderCreationPort(OrderService orderServiceClient) {
        return new OrderCreationAdapter(orderServiceClient);
    }

    @Bean
    PriceLockPort checkoutPriceLockPort(PricingService pricingServiceClient) {
        return new CheckoutPriceLockAdapter(pricingServiceClient);
    }

    @Bean
    PromoCommitPort checkoutPromoCommitPort(PromotionService promotionServiceClient) {
        return new PromoCommitAdapter(promotionServiceClient);
    }

    @Bean
    ShippingValidationPort checkoutShippingValidationPort(
            @Qualifier("shippingServiceClient") StateEntityService<?> shippingServiceClient) {
        return new ShippingValidationAdapter(shippingServiceClient);
    }
}
