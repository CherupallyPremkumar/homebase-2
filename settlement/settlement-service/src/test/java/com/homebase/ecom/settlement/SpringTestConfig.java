package com.homebase.ecom.settlement;

import com.homebase.ecom.shared.Currency;
import com.homebase.ecom.shared.CurrencyResolver;
import org.chenile.cconfig.sdk.CconfigClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:com/homebase/ecom/settlement/TestService.properties")
@SpringBootApplication(scanBasePackages = {
        "org.chenile.configuration",
        "org.chenile.service.registry.configuration",
        "com.homebase.ecom.settlement.configuration",
        "com.homebase.ecom.settlement.infrastructure"
})
@EnableJpaRepositories(basePackages = {"com.homebase.ecom.settlement.infrastructure.persistence.adapter", "org.chenile.service.registry.configuration.dao"})
@EntityScan(basePackages = {"com.homebase.ecom.settlement", "org.chenile.service.registry.model"})
@ActiveProfiles("unittest")
public class SpringTestConfig {

    @Bean
    @Primary
    public CurrencyResolver testCurrencyResolver() {
        return new CurrencyResolver() {
            @Override
            public Currency resolve() {
                return new Currency("INR", "\u20B9", 2);
            }

            @Override
            public Currency resolve(Map<String, Object> context) {
                return resolve();
            }

            @Override
            public void initContext(Map<String, Object> context) {
                // no-op for test
            }

            @Override
            public Currency getMetadata(String code) {
                return resolve();
            }
        };
    }

    @Bean
    @Primary
    public CconfigClient testCconfigClient() {
        return new CconfigClient() {
            @Override
            public Map<String, Object> value(String configName, String tenant) {
                if ("settlement".equals(configName)) {
                    Map<String, Object> config = new HashMap<>();
                    Map<String, Object> policies = new HashMap<>();
                    policies.put("commissionRatePercent", 15);
                    policies.put("platformFeePercent", 2);
                    policies.put("settlementCycleDays", 14);
                    policies.put("minSettlementAmount", 500);
                    policies.put("maxAdjustmentPercent", 10);
                    policies.put("autoApproveThreshold", 10000);
                    config.put("policies", policies);
                    return config;
                }
                return new HashMap<>();
            }
        };
    }
}
