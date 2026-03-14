package com.homebase.ecom.policy.domain.repository;

import com.homebase.ecom.policy.domain.model.Policy;
import java.util.List;
import java.util.Optional;

public interface PolicyRepository {
    Policy save(Policy policy);
    Optional<Policy> findById(String id);
    List<Policy> findAll();
    List<Policy> findByTargetModuleAndActiveTrue(String targetModule);
    void deleteById(String id);
}
