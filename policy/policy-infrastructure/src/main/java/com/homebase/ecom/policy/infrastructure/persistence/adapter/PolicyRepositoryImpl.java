package com.homebase.ecom.policy.infrastructure.persistence.adapter;

import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.domain.repository.PolicyRepository;
import com.homebase.ecom.policy.infrastructure.persistence.entity.PolicyEntity;
import com.homebase.ecom.policy.infrastructure.persistence.repository.PolicyJpaRepository;
import com.homebase.ecom.policy.infrastructure.persistence.mapper.PolicyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PolicyRepositoryImpl implements PolicyRepository {

    private final PolicyJpaRepository policyJpaRepository;
    private final PolicyMapper policyMapper;

    public PolicyRepositoryImpl(PolicyJpaRepository policyJpaRepository, PolicyMapper policyMapper) {
        this.policyJpaRepository = policyJpaRepository;
        this.policyMapper = policyMapper;
    }

    @Override
    public Optional<Policy> findById(String id) {
        return policyJpaRepository.findById(id).map(policyMapper::toModel);
    }

    @Override
    public List<Policy> findAll() {
        return policyJpaRepository.findAll().stream()
                .map(policyMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Policy> findByTargetModuleAndActiveTrue(String targetModule) {
        return policyJpaRepository.findByTargetModuleAndActiveTrue(targetModule).stream()
                .map(policyMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Policy save(Policy policy) {
        PolicyEntity entity = policyMapper.toEntity(policy);
        PolicyEntity savedEntity = policyJpaRepository.save(entity);
        return policyMapper.toModel(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        policyJpaRepository.deleteById(id);
    }
}
