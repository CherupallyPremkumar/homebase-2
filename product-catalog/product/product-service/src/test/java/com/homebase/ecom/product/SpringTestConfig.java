package com.homebase.ecom.product;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.kafka.core.KafkaTemplate;

import com.homebase.ecom.product.service.event.ProductEventHandler;

import static org.mockito.Mockito.mock;

@Configuration
@PropertySource("classpath:com/homebase/ecom/product/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "org.chenile.service.registry.configuration", "com.homebase.ecom.product.configuration" })
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.product", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.product", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig {

    @SuppressWarnings("unchecked")
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return mock(KafkaTemplate.class);
    }

    @Bean
    public ProductEventHandler productEventHandler() {
        return mock(ProductEventHandler.class);
    }
}
