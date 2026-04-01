package com.homebase.ecom.compliance.client;

import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import org.chenile.workflow.api.StateEntityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComplianceClientConfiguration {

    @Bean
    ComplianceClient complianceClient(
            @Qualifier("_agreementStateEntityService_") StateEntityService<Agreement> agreementService,
            @Qualifier("_platformPolicyStateEntityService_") StateEntityService<PlatformPolicy> platformPolicyService) {
        return new ComplianceClientImpl(agreementService, platformPolicyService);
    }
}
