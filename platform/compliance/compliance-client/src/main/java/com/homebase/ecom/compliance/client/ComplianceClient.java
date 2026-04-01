package com.homebase.ecom.compliance.client;

import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import org.chenile.workflow.dto.StateEntityServiceResponse;

public interface ComplianceClient {
    StateEntityServiceResponse<Agreement> getAgreement(String id);
    StateEntityServiceResponse<PlatformPolicy> getPlatformPolicy(String id);
}
