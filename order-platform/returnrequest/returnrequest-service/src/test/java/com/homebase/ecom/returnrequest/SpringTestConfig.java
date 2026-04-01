package com.homebase.ecom.returnrequest;

import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.chenile.pubsub.ChenilePub;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@Configuration
@PropertySource("classpath:com/homebase/ecom/returnrequest/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.**",
    "com.homebase.ecom.returnrequest.**"
})
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.returnrequest.infrastructure.persistence.repository",
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.returnrequest.infrastructure.persistence.entity",
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

    /**
     * Provides a test CconfigClient that returns return request policies.
     * returnWindowDays(30), maxReturnItemsPerOrder(10), autoApproveUnderValue(500),
     * inspectionRequiredAboveValue(5000), restockingFeePercent(0)
     */
    @Bean
    public CconfigClient cconfigClient() {
        return (module, key) -> {
            if ("returnrequest".equals(module)) {
                Map<String, Object> config = new LinkedHashMap<>();

                // Policies
                Map<String, Object> policies = new LinkedHashMap<>();

                Map<String, Object> returnPolicies = new LinkedHashMap<>();
                returnPolicies.put("returnWindowDays", 30);
                returnPolicies.put("maxReturnItemsPerOrder", 10);
                returnPolicies.put("requireReturnReason", true);
                returnPolicies.put("allowedReturnReasons", Arrays.asList("DAMAGED", "DEFECTIVE", "WRONG_ITEM", "NOT_AS_DESCRIBED", "SIZE_ISSUE"));
                policies.put("return", returnPolicies);

                Map<String, Object> approvalPolicies = new LinkedHashMap<>();
                approvalPolicies.put("autoApproveUnderValue", 500.0);
                policies.put("approval", approvalPolicies);

                Map<String, Object> inspectionPolicies = new LinkedHashMap<>();
                inspectionPolicies.put("inspectionRequiredAboveValue", 5000.0);
                policies.put("inspection", inspectionPolicies);

                Map<String, Object> feePolicies = new LinkedHashMap<>();
                feePolicies.put("restockingFeePercent", 0.0);
                policies.put("fee", feePolicies);

                config.put("policies", policies);

                if (key != null) {
                    if (config.containsKey(key)) {
                        return new HashMap<>(Map.of(key, config.get(key)));
                    }
                    return new HashMap<>();
                }
                return config;
            }
            return new HashMap<>();
        };
    }
}
