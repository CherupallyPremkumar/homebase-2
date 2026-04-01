package com.homebase.ecom.product.infrastructure.configuration;

import com.homebase.ecom.product.domain.port.ImageProcessingPort;
import com.homebase.ecom.product.domain.port.ObjectStoragePort;
import com.homebase.ecom.product.domain.port.PimPolicyPort;
import com.homebase.ecom.product.infrastructure.adapter.PolicyDecisionAdapter;
import com.homebase.ecom.product.infrastructure.integration.LoggingImageProcessingAdapter;
import com.homebase.ecom.product.infrastructure.integration.LoggingObjectStorageAdapter;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer configuration for the Product bounded context.
 * Wires adapter implementations to domain port interfaces.
 *
 * <p>All beans are module-prefixed to avoid collisions in the monolith.</p>
 */
@Configuration
public class ProductInfrastructureConfiguration {

    @Bean("productPimPolicyPort")
    PimPolicyPort productPimPolicyPort(DecisionService decisionServiceClient) {
        return new PolicyDecisionAdapter(decisionServiceClient);
    }

    @Bean("productImageProcessingPort")
    ImageProcessingPort productImageProcessingPort() {
        return new LoggingImageProcessingAdapter();
    }

    @Bean("productObjectStoragePort")
    ObjectStoragePort productObjectStoragePort() {
        return new LoggingObjectStorageAdapter();
    }
}
