package com.homebase.ecom.cart.infrastructure.test;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import com.homebase.ecom.cart.port.ConfigPort;
import com.homebase.ecom.cart.port.InventoryCheckPort;
import com.homebase.ecom.cart.port.PricingPort;
import com.homebase.ecom.cart.port.ProductCheckPort;
import com.homebase.ecom.cart.port.response.PricingResult;
import com.homebase.ecom.cart.repository.CartRepository;
import com.homebase.ecom.shared.Money;
import org.chenile.pubsub.ChenilePub;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Test stub configuration for cart infrastructure.
 * Provides in-memory implementations of all ports — no DB, no Kafka, no cconfig.
 *
 * Usage: depend on cart-infrastructure test-jar in your module's test scope.
 */
@Configuration
public class CartInfrastructureTestConfiguration {

    // ═══════════════════════════════════════════════════════════════════
    // ConfigPort — hardcoded defaults
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    ConfigPort cartConfigPort() {
        return new ConfigPort() {
            @Override public int getMaxItemsPerCart() { return 30; }
            @Override public int getMaxQuantityPerItem() { return 10; }
            @Override public int getMaxCouponsPerCart() { return 3; }
            @Override public long getMinCheckoutAmount() { return 100L; }
            @Override public long getMaxCartValue() { return 10_000_000L; }
            @Override public int getCartExpirationHours() { return 72; }
            @Override public int getAbandonmentThresholdHours() { return 24; }
            @Override public int getCheckoutReservationMinutes() { return 15; }
            @Override public boolean isGuestCheckoutAllowed() { return true; }
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    // PricingPort — simple multiplication
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    PricingPort cartPricingPort() {
        return cart -> {
            String currency = cart.getCurrency();
            List<PricingResult.LineItemPricing> lineItems = cart.getItems().stream().map(item -> {
                long lineTotal = item.getUnitPrice().getAmount() * item.getQuantity();
                PricingResult.LineItemPricing lp = new PricingResult.LineItemPricing();
                lp.setVariantId(item.getVariantId());
                lp.setUnitPrice(item.getUnitPrice());
                lp.setLineTotal(new Money(lineTotal, currency));
                return lp;
            }).collect(Collectors.toList());

            long subtotal = lineItems.stream()
                    .mapToLong(lp -> lp.getLineTotal().getAmount())
                    .sum();

            PricingResult result = new PricingResult();
            result.setSubtotal(new Money(subtotal, currency));
            result.setTotalDiscount(Money.zero(currency));
            result.setTotal(new Money(subtotal, currency));
            result.setLineItems(lineItems);
            return result;
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    // InventoryCheckPort — always available
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    InventoryCheckPort cartInventoryCheckPort() {
        return new InventoryCheckPort() {
            @Override
            public boolean isAvailable(String variantId, int quantity) { return true; }
            @Override
            public int getAvailableQuantity(String variantId) { return 1000; }
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    // ProductCheckPort — known test products
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    ProductCheckPort cartProductCheckPort() {
        Map<String, Set<String>> publishedProducts = Map.of(
            "prod-1", Set.of("var-1a", "var-1b"),
            "prod-2", Set.of("var-2a"),
            "prod-5", Set.of("var-5a"),
            "prod-first-001", Set.of("var-first-001-default"),
            "prod-test2-001", Set.of("var-test2-001-default"),
            "prod-policy-001", Set.of("var-policy-001-default"),
            "prod-policy-excess", Set.of("var-policy-excess-default")
        );
        return (productId, variantId) -> {
            var variants = publishedProducts.get(productId);
            return variants != null && variants.contains(variantId);
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    // CartEventPublisherPort — no-op (no events in tests)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    CartEventPublisherPort cartEventPublisherPort() {
        return event -> {};
    }

    // ═══════════════════════════════════════════════════════════════════
    // ChenilePub — no-op (no Kafka in tests)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    ChenilePub cartChenilePub() {
        return new ChenilePub() {
            @Override
            public void publishToOperation(String service, String operationName, String payload, Map<String, Object> properties) {}
            @Override
            public void publish(String topic, String payload, Map<String, Object> properties) {}
            @Override
            public void asyncPublish(String topic, String payload, Map<String, Object> properties) {}
        };
    }
}
