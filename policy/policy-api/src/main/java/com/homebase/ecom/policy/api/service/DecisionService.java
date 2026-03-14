package com.homebase.ecom.policy.api.service;

import com.homebase.ecom.policy.api.dto.EvaluateRequest;
import com.homebase.ecom.policy.api.dto.DecisionDto;

import java.util.List;

public interface DecisionService {
    DecisionDto evaluate(EvaluateRequest request);
    List<DecisionDto> getDecisions();
    List<DecisionDto> getDecisionsByPolicyId(String policyId);
}
