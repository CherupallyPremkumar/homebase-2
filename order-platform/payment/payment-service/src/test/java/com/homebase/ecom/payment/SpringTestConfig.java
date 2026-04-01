package com.homebase.ecom.payment;

import com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@PropertySource("classpath:com/homebase/ecom/payment/TestService.properties")
@SpringBootApplication(scanBasePackages = {
        "org.chenile",
        "com.homebase.ecom.payment.configuration"
})
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.payment.infrastructure", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.payment.infrastructure", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig {

    @Bean
    public PaymentEventPublisherPort paymentEventPublisherPort() {
        return Mockito.mock(PaymentEventPublisherPort.class);
    }
}
