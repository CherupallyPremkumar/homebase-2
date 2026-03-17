package com.homebase.ecom.checkout;

import com.homebase.ecom.checkout.domain.port.*;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.shared.Money;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
@PropertySource("classpath:com/homebase/ecom/checkout/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.checkout.configuration",
    "com.homebase.ecom.checkout.infrastructure.persistence",
    "com.homebase.ecom.checkout.infrastructure.mapper",
    "com.homebase.ecom.checkout.bdd",
    "org.chenile.configuration.security"
})
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.checkout", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.checkout", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {

    // ═══════════════════════════════════════════════════════════════════
    // Test port implementations — stub adapters for BDD tests
    // Tests exercise Checkout STM + saga logic, not real service calls
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    CartLockPort cartLockPort() {
        return new CartLockPort() {
            @Override
            public CartSnapshot lockCart(String cartId) {
                return new CartSnapshot(
                    "customer-001", "INR", 99800, 0, 99800,
                    List.of(),
                    List.of(
                        new CartItemSnapshot("prod-1", "var-1a", "SKU-001",
                            "Test Widget", "supplier-1", 2, 49900, 99800)
                    )
                );
            }

            @Override
            public void unlockCart(String cartId) {
                // no-op in tests
            }
        };
    }

    @Bean
    PriceLockPort priceLockPort() {
        return (cartId, customerId, currency, items, couponCodes) -> {
            long subtotal = items.stream()
                    .mapToLong(i -> i.getUnitPrice().getAmount() * i.getQuantity())
                    .sum();
            long tax = Math.round(subtotal * 0.18);
            long shipping = 5000;
            long finalTotal = subtotal + tax + shipping;
            return new PriceLockPort.LockedPrice(
                subtotal, 0, tax, shipping, finalTotal,
                "lock-" + UUID.randomUUID(), "hash-" + cartId, currency
            );
        };
    }

    @Bean
    InventoryReservePort inventoryReservePort() {
        return new InventoryReservePort() {
            @Override public void reserve(String checkoutId, Map<String, Integer> variantQuantities) {}
            @Override public void release(String checkoutId) {}
        };
    }

    @Bean
    ShippingValidationPort shippingValidationPort() {
        return (shippingAddressId, shippingMethod, currency) ->
                new ShippingValidationPort.ShippingResult(true, 5000, "3-5 business days", null);
    }

    @Bean
    OrderCreationPort orderCreationPort() {
        return new OrderCreationPort() {
            @Override
            public String createOrder(Checkout checkout) {
                return "order-" + UUID.randomUUID().toString().substring(0, 8);
            }

            @Override
            public void cancelOrder(String orderId) {}
        };
    }

    @Bean
    PromoCommitPort promoCommitPort() {
        return new PromoCommitPort() {
            @Override public void commitCoupons(String checkoutId, String customerId, List<String> couponCodes) {}
            @Override public void releaseCoupons(String checkoutId, List<String> couponCodes) {}
        };
    }

    @Bean
    PaymentInitiationPort paymentInitiationPort() {
        return new PaymentInitiationPort() {
            @Override
            public PaymentResult initiatePayment(String checkoutId, String orderId,
                    Money amount, String customerId, String paymentMethodId) {
                return new PaymentResult(
                    "pay-" + UUID.randomUUID().toString().substring(0, 8),
                    "rzp_order_" + checkoutId, "created"
                );
            }

            @Override
            public void cancelPayment(String paymentId) {}
        };
    }
}
