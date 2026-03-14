package com.homebase.ecom.settlement;

import com.homebase.ecom.settlement.service.client.InternalOrderClient;
import com.homebase.ecom.settlement.service.client.StubInternalOrderClient;
import com.homebase.ecom.shared.Currency;
import com.homebase.ecom.shared.CurrencyResolver;
import org.chenile.cconfig.sdk.CconfigClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:com/homebase/ecom/settlement/TestService.properties")
@SpringBootApplication(scanBasePackages = {
        "org.chenile.configuration",
        "com.homebase.ecom.settlement.configuration",
        "com.homebase.ecom.settlement.infrastructure"
})
@EnableJpaRepositories(basePackages = "com.homebase.ecom.settlement.infrastructure.persistence.adapter")
@ActiveProfiles("unittest")
public class SpringTestConfig {

    @Bean
    @Primary
    public InternalOrderClient stubInternalOrderClient() {
        return new StubInternalOrderClient();
    }

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
                    Map<String, Object> rules = new HashMap<>();
                    Map<String, Object> payout = new HashMap<>();
                    payout.put("minBalance", 1000);
                    rules.put("payout", payout);
                    config.put("rules", rules);

                    Map<String, Object> policies = new HashMap<>();
                    Map<String, Object> payoutPolicies = new HashMap<>();
                    payoutPolicies.put("minPayoutBalanceInr", 1000);
                    policies.put("payout", payoutPolicies);
                    config.put("policies", policies);
                    return config;
                }
                if ("on-boarding".equals(configName)) {
                    Map<String, Object> config = new HashMap<>();
                    Map<String, Object> rules = new HashMap<>();
                    Map<String, Object> finances = new HashMap<>();
                    finances.put("commissionDefault", 10); // 10% commission
                    rules.put("finances", finances);
                    config.put("rules", rules);
                    return config;
                }
                return new HashMap<>();
            }
        };
    }
}
