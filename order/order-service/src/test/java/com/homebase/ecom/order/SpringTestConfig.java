package com.homebase.ecom.order;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import com.homebase.ecom.order.service.event.OrderEventPublisher;

@Configuration
@PropertySource("classpath:com/homebase/ecom/order/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "org.chenile.service.registry.configuration", "com.homebase.ecom.order.configuration" })
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.order", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.order", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig {

    @Bean
    public OrderEventPublisher orderEventPublisher() {
        return Mockito.mock(OrderEventPublisher.class);
    }
}
