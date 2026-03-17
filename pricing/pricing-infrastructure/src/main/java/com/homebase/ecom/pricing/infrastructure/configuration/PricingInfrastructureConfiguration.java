package com.homebase.ecom.pricing.infrastructure.configuration;

import com.homebase.ecom.pricing.domain.port.PriceLockPort;
import com.homebase.ecom.pricing.domain.port.PolicyEvaluationPort;
import com.homebase.ecom.pricing.domain.service.IHashCalculator;
import com.homebase.ecom.pricing.domain.service.ILockTokenGenerator;
import com.homebase.ecom.pricing.infrastructure.cache.PriceLockAdapter;
import com.homebase.ecom.pricing.infrastructure.cache.PriceLockCacheManager;
import com.homebase.ecom.pricing.infrastructure.client.PolicyEvaluationAdapter;
import com.homebase.ecom.pricing.infrastructure.security.HashCalculator;
import com.homebase.ecom.pricing.infrastructure.security.LockTokenGenerator;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer bean definitions for pricing adapters.
 * Owns all adapter wiring — service layer only references domain ports/interfaces.
 */
@Configuration
public class PricingInfrastructureConfiguration {

    @Bean
    @ConditionalOnMissingBean(PolicyEvaluationPort.class)
    PolicyEvaluationPort policyEvaluationPort(DecisionService decisionServiceClient) {
        return new PolicyEvaluationAdapter(decisionServiceClient);
    }

    @Bean
    @ConditionalOnMissingBean(IHashCalculator.class)
    IHashCalculator hashCalculator() {
        return new HashCalculator();
    }

    @Bean
    @ConditionalOnMissingBean(ILockTokenGenerator.class)
    ILockTokenGenerator lockTokenGenerator() {
        return new LockTokenGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(PriceLockPort.class)
    @ConditionalOnBean(PriceLockCacheManager.class)
    PriceLockPort priceLockPort(PriceLockCacheManager priceLockCacheManager) {
        return new PriceLockAdapter(priceLockCacheManager);
    }
}
