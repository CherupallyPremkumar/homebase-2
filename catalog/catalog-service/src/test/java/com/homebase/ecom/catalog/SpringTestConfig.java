package com.homebase.ecom.catalog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import com.homebase.ecom.cconfig.sdk.CconfigClient;
import com.homebase.ecom.offer.api.OfferService;
import com.homebase.ecom.product.api.ProductService;
import org.chenile.query.service.SearchService;
import org.chenile.security.service.SecurityService;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;

@Configuration
@PropertySource("classpath:com/homebase/ecom/catalog/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.catalog.configuration",
    "com.homebase.ecom.catalog.controller"
})
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.catalog.infrastructure.persistence.repository",
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.catalog.infrastructure.persistence.entity",
    "org.chenile.service.registry.model"
})
@ActiveProfiles("unittest")
public class SpringTestConfig {

    // ── Stub beans for cross-BC dependencies (test only) ─────────────

    @SuppressWarnings("unchecked")
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return mock(KafkaTemplate.class);
    }

    @Bean
    public ProductService productService() {
        return mock(ProductService.class);
    }

    @Bean
    public OfferService offerService() {
        return mock(OfferService.class);
    }

    @Bean
    public CconfigClient cconfigClient() {
        return mock(CconfigClient.class);
    }

    @Bean
    public SearchService productSearchService() {
        return mock(SearchService.class);
    }

    @Bean
    public SecurityService securityService() {
        return mock(SecurityService.class);
    }
}
