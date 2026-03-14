package com.homebase.ecom.returnrequest;

import org.chenile.cconfig.sdk.CconfigClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@Configuration
@PropertySource("classpath:com/homebase/ecom/returnrequest/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.returnrequest.configuration", "com.homebase.ecom.returnrequest.service" })
@ActiveProfiles("unittest")
public class SpringTestConfig {

    /**
     * Provides a test CconfigClient that returns default return request policies.
     * This avoids requiring the full cconfig infrastructure in unit tests.
     */
    @Bean
    public CconfigClient cconfigClient() {
        return (module, key) -> {
            if ("returnrequest".equals(module)) {
                Map<String, Object> config = new LinkedHashMap<>();

                // Policies
                Map<String, Object> policies = new LinkedHashMap<>();
                Map<String, Object> returnPolicies = new LinkedHashMap<>();
                returnPolicies.put("maxReturnWindowDays", 10);
                returnPolicies.put("requireReturnReason", true);
                returnPolicies.put("allowedReturnReasons", Arrays.asList("DAMAGED", "DEFECTIVE", "WRONG_ITEM", "NOT_AS_DESCRIBED"));
                returnPolicies.put("hubInspectionRequired", true);
                policies.put("return", returnPolicies);

                Map<String, Object> approvalPolicies = new LinkedHashMap<>();
                approvalPolicies.put("autoApproveBelow", 500.0);
                approvalPolicies.put("requireHubManagerApprovalAbove", 5000.0);
                policies.put("approval", approvalPolicies);

                config.put("policies", policies);

                // Rules
                Map<String, Object> rules = new LinkedHashMap<>();
                Map<String, Object> refundRules = new LinkedHashMap<>();
                refundRules.put("autoRefundOnHubReceipt", true);
                refundRules.put("refundProcessingDays", 3);
                rules.put("refund", refundRules);

                Map<String, Object> auditRules = new LinkedHashMap<>();
                auditRules.put("requireCommentOnReject", true);
                rules.put("audit", auditRules);

                config.put("rules", rules);

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
