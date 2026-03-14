package com.homebase.ecom.policy.domain.repository;

import com.homebase.ecom.policy.domain.model.Decision;

import java.util.List;
import java.util.Optional;

public interface DecisionRepository {
    Decision save(Decision decision);
    Optional<Decision> findById(String id);
    List<Decision> findAll();
    List<Decision> findByPolicyId(String policyId);
}
