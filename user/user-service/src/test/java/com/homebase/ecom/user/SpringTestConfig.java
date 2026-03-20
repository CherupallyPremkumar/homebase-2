package com.homebase.ecom.user;

import org.chenile.pubsub.ChenilePub;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@Configuration
@PropertySource("classpath:com/homebase/ecom/user/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.**",
    "com.homebase.ecom.user.**"
})
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.user.infrastructure.persistence.adapter",
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.user.infrastructure.persistence.entity",
    "org.chenile.service.registry.model"
})
@ActiveProfiles("unittest")
public class SpringTestConfig {

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
}
