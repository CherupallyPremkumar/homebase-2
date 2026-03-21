package com.homebase.ecom.promo;

import com.homebase.ecom.promo.port.PromoEventPublisherPort;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:com/homebase/ecom/promo/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "org.chenile.service.registry.configuration", "com.homebase.ecom.promo.configuration", "com.homebase.ecom.cconfig.**" })
@EnableJpaRepositories(basePackages = {"com.homebase.ecom.promo", "com.homebase.ecom.cconfig.configuration", "org.chenile.service.registry.configuration.dao"})
@EntityScan(basePackages = {"com.homebase.ecom.promo", "org.chenile.service.registry.model", "com.homebase.ecom.cconfig.model"})
@ActiveProfiles("unittest")
public class SpringTestConfig {

    /**
     * Stub PromoEventPublisherPort for unit tests — no-op, does not publish to Kafka.
     */
    @Bean
    PromoEventPublisherPort promoEventPublisherPort() {
        return new PromoEventPublisherPort() {
            @Override
            public void publishPromoActivated(com.homebase.ecom.promo.model.Coupon coupon) {
                // no-op in tests
            }

            @Override
            public void publishPromoExpired(com.homebase.ecom.promo.model.Coupon coupon) {
                // no-op in tests
            }
        };
    }
}
