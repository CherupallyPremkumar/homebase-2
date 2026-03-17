package com.homebase.ecom.cart;

import com.homebase.ecom.cart.domain.model.PricingResult;
import com.homebase.ecom.cart.domain.port.InventoryCheckPort;
import com.homebase.ecom.cart.domain.port.PricingPort;
import com.homebase.ecom.cart.domain.port.ProductCheckPort;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.Money;
import org.chenile.pubsub.ChenilePub;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
@SpringBootApplication(
	scanBasePackages = {
		"org.chenile.**",
			"org.chenile.configuration",
		"com.homebase.ecom.cart.**",
			"org.chenile.service.registry.configuration",
			"org.chenile.pubsub.kafka.configuration"
	}
)
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.cart", "org.chenile.service.registry.configuration.dao", "com.homebase.ecom.cconfig.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.cart", "org.chenile.service.registry.model", "com.homebase.ecom.cconfig.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {

	// ═══════════════════════════════════════════════════════════════════
	// Test port implementations — no ProxyBuilder, no service-registry
	// Tests exercise Cart domain logic, not Chenile proxy infrastructure
	// ═══════════════════════════════════════════════════════════════════

	@Bean
	PricingPort pricingPort() {
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

	@Bean
	InventoryCheckPort inventoryCheckPort() {
		return new InventoryCheckPort() {
			@Override
			public boolean isAvailable(String variantId, int quantity) {
				return true;
			}

			@Override
			public int getAvailableQuantity(String variantId) {
				return 1000;
			}
		};
	}

	@Bean
	@Primary
	ChenilePub chenilePub() {
		return new ChenilePub() {
			@Override
			public void publishToOperation(String service, String operationName, String payload, Map<String, Object> properties) {}
			@Override
			public void publish(String topic, String payload, Map<String, Object> properties) {}
			@Override
			public void asyncPublish(String topic, String payload, Map<String, Object> properties) {}
		};
	}

	@Bean
	ProductCheckPort productCheckPort() {
		// Test data: prod-1 (PUBLISHED: var-1a, var-1b), prod-2 (PUBLISHED: var-2a),
		// prod-5 (PUBLISHED: var-5a), prod-4 (DRAFT — NOT sellable)
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
}
