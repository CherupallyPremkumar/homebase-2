package com.homebase.ecom.pricing;

import com.homebase.ecom.pricing.domain.model.LockedPriceBreakdown;
import com.homebase.ecom.pricing.domain.port.PriceLockPort;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@PropertySource("classpath:com/homebase/ecom/pricing/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.pricing.service.configuration",
    "com.homebase.ecom.pricing.infrastructure",
    "com.homebase.ecom.pricing.bdd",
    "org.chenile.configuration.security"
})
@EnableJpaRepositories(basePackages = { "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {

    /**
     * In-memory PriceLockPort for tests — no Redis needed.
     */
    @Bean
    @Primary
    PriceLockPort testPriceLockPort() {
        return new InMemoryPriceLockPort();
    }

    static class InMemoryPriceLockPort implements PriceLockPort {
        private final Map<String, LockedPriceBreakdown> store = new ConcurrentHashMap<>();

        @Override
        public void store(LockedPriceBreakdown lock) {
            store.put(lock.getLockToken(), lock);
        }

        @Override
        public Optional<LockedPriceBreakdown> retrieve(String lockToken) {
            return Optional.ofNullable(store.get(lockToken));
        }

        @Override
        public boolean isValid(String lockToken) {
            LockedPriceBreakdown lock = store.get(lockToken);
            return lock != null && !lock.isExpired();
        }

        @Override
        public void invalidate(String lockToken) {
            store.remove(lockToken);
        }
    }
}
