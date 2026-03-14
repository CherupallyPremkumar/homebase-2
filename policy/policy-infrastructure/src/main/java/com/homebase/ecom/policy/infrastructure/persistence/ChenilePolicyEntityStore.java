package com.homebase.ecom.policy.infrastructure.persistence;

import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.domain.repository.PolicyRepository;
import org.chenile.base.exception.NotFoundException;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class ChenilePolicyEntityStore implements EntityStore<Policy> {
    
    private PolicyRepository policyRepository;

    public ChenilePolicyEntityStore(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public void store(Policy entity) {
        policyRepository.save(entity);
    }

    @Override
    public Policy retrieve(String id) {
        Optional<Policy> entity = policyRepository.findById(id);
        if (entity.isPresent())
            return entity.get();
        throw new NotFoundException(1600, "Unable to find Policy with ID " + id);
    }
}
