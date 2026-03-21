package com.homebase.ecom.checkout.infrastructure.test;

import com.homebase.ecom.checkout.domain.port.CartLockPort;
import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.domain.port.InventoryReservePort;
import com.homebase.ecom.checkout.domain.port.OrderCreationPort;
import com.homebase.ecom.checkout.domain.port.PaymentInitiationPort;
import com.homebase.ecom.checkout.domain.port.PriceLockPort;
import com.homebase.ecom.checkout.domain.port.PromoCommitPort;
import com.homebase.ecom.checkout.domain.port.ShippingValidationPort;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.shared.Money;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Test stub configuration for checkout infrastructure.
 * Provides no-op / stub implementations of all ports -- no DB, no Kafka, no real services.
 *
 * Usage: depend on checkout-infrastructure test-jar in your module's test scope.
 */
@Configuration
public class CheckoutInfrastructureTestConfiguration {

    // ═══════════════════════════════════════════════════════════════════
    // CheckoutEventPublisherPort -- no-op (no events in tests)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    CheckoutEventPublisherPort checkoutEventPublisherPort() {
        return event -> {};
    }

    // ═══════════════════════════════════════════════════════════════════
    // CartLockPort -- returns test snapshot
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
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

    // ═══════════════════════════════════════════════════════════════════
    // PriceLockPort -- simple calculation
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
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

    // ═══════════════════════════════════════════════════════════════════
    // InventoryReservePort -- no-op
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    InventoryReservePort inventoryReservePort() {
        return new InventoryReservePort() {
            @Override public void reserve(String checkoutId, Map<String, Integer> variantQuantities) {}
            @Override public void release(String checkoutId) {}
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    // ShippingValidationPort -- always valid
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    ShippingValidationPort shippingValidationPort() {
        return (shippingAddressId, shippingMethod, currency) ->
                new ShippingValidationPort.ShippingResult(true, 5000, "3-5 business days", null);
    }

    // ═══════════════════════════════════════════════════════════════════
    // OrderCreationPort -- returns fake order ID
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
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

    // ═══════════════════════════════════════════════════════════════════
    // PromoCommitPort -- no-op
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    PromoCommitPort promoCommitPort() {
        return new PromoCommitPort() {
            @Override public void commitCoupons(String checkoutId, String customerId, List<String> couponCodes) {}
            @Override public void releaseCoupons(String checkoutId, List<String> couponCodes) {}
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    // PaymentInitiationPort -- returns fake payment result
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
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

    // ═══════════════════════════════════════════════════════════════════
    // ChenilePub -- no-op (no Kafka in tests)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @Primary
    ChenilePub checkoutChenilePub() {
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
