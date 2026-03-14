package com.homebase.ecom.policy.infrastructure.persistence.adapter;

import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.domain.repository.PolicyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that bridges the Domain Repository with Spring Data JPA.
 */
public class PolicyRepositoryAdapter {
    private final PolicyRepository policyRepository;

    public PolicyRepositoryAdapter(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public Policy save(Policy policy) {
        return policyRepository.save(policy);
    }

    public Optional<Policy> findById(String id) {
        return policyRepository.findById(id);
    }

    public List<Policy> findAll() {
        return policyRepository.findAll();
    }

    public void deleteById(String id) {
        policyRepository.deleteById(id);
    }
}
