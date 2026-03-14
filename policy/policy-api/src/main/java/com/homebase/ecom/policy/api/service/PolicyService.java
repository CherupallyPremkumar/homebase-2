package com.homebase.ecom.policy.api.service;

import com.homebase.ecom.policy.api.dto.PolicyDto;
import java.util.List;

public interface PolicyService {
    PolicyDto createPolicy(PolicyDto policyDto);
    PolicyDto getPolicy(String id);
    List<PolicyDto> listPolicies();
    PolicyDto updatePolicy(String id, PolicyDto policyDto);
    void deletePolicy(String id);
    
    // STM Transitions
    PolicyDto submitPolicy(String id);
    PolicyDto approvePolicy(String id);
    PolicyDto activatePolicy(String id);
    PolicyDto deprecatePolicy(String id);

}
