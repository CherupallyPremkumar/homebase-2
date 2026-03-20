package com.homebase.ecom.compliance.client;

import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.chenile.workflow.api.StateEntityService;

public class ComplianceClientImpl implements ComplianceClient {

    private final StateEntityService<Agreement> agreementService;
    private final StateEntityService<PlatformPolicy> platformPolicyService;

    public ComplianceClientImpl(
            StateEntityService<Agreement> agreementService,
            StateEntityService<PlatformPolicy> platformPolicyService) {
        this.agreementService = agreementService;
        this.platformPolicyService = platformPolicyService;
    }

    @Override
    public StateEntityServiceResponse<Agreement> getAgreement(String id) {
        return agreementService.retrieve(id);
    }

    @Override
    public StateEntityServiceResponse<PlatformPolicy> getPlatformPolicy(String id) {
        return platformPolicyService.retrieve(id);
    }
}
