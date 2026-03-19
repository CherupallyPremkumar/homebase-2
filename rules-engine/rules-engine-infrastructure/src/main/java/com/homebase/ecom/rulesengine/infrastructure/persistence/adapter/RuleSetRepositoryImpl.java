package com.homebase.ecom.rulesengine.infrastructure.persistence.adapter;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;
import com.homebase.ecom.rulesengine.infrastructure.persistence.entity.RuleSetEntity;
import com.homebase.ecom.rulesengine.infrastructure.persistence.entity.RuleEntity;
import com.homebase.ecom.rulesengine.infrastructure.persistence.repository.RuleSetJpaRepository;
import com.homebase.ecom.rulesengine.infrastructure.persistence.mapper.RuleSetMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RuleSetRepositoryImpl implements RuleSetRepository {

    private final RuleSetJpaRepository ruleSetJpaRepository;
    private final RuleSetMapper ruleSetMapper;

    public RuleSetRepositoryImpl(RuleSetJpaRepository ruleSetJpaRepository, RuleSetMapper ruleSetMapper) {
        this.ruleSetJpaRepository = ruleSetJpaRepository;
        this.ruleSetMapper = ruleSetMapper;
    }

    @Override
    public Optional<RuleSet> findById(String id) {
        return ruleSetJpaRepository.findByIdWithRules(id).map(ruleSetMapper::toModel);
    }

    @Override
    public List<RuleSet> findAll() {
        return ruleSetJpaRepository.findAllWithRules().stream()
                .map(ruleSetMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<RuleSet> findByTargetModuleAndActiveTrue(String targetModule) {
        return ruleSetJpaRepository.findByTargetModuleAndActiveTrue(targetModule).stream()
                .map(ruleSetMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public RuleSet save(RuleSet ruleSet) {
        RuleSetEntity entity;
        if (ruleSet.getId() != null) {
            Optional<RuleSetEntity> existing = ruleSetJpaRepository.findByIdWithRules(ruleSet.getId());
            if (existing.isPresent()) {
                // Update existing entity in-place to preserve JPA @Version
                entity = existing.get();
                entity.setName(ruleSet.getName());
                entity.setDescription(ruleSet.getDescription());
                entity.setDefaultEffect(ruleSet.getDefaultEffect());
                entity.setActive(ruleSet.isActive());
                entity.setTargetModule(ruleSet.getTargetModule());
                entity.tenant = ruleSet.getTenant();
                // STM state fields — set by GenericEntryAction before store()
                entity.setCurrentState(ruleSet.getCurrentState());
                if (ruleSet.getStateEntryTime() != null) entity.setStateEntryTime(ruleSet.getStateEntryTime());
                if (ruleSet.getLastModifiedTime() != null) entity.setLastModifiedTime(ruleSet.getLastModifiedTime());

                // Sync rules: clear existing and re-add
                entity.getRules().clear();
                if (ruleSet.getRules() != null) {
                    for (var rule : ruleSet.getRules()) {
                        RuleEntity re = ruleSetMapper.toEntity(rule);
                        re.setRuleSet(entity);
                        entity.getRules().add(re);
                    }
                }
            } else {
                entity = ruleSetMapper.toEntity(ruleSet);
            }
        } else {
            entity = ruleSetMapper.toEntity(ruleSet);
        }
        RuleSetEntity savedEntity = ruleSetJpaRepository.save(entity);
        return ruleSetMapper.toModel(savedEntity);
    }

    @Override
    public List<RuleSet> findByTargetModuleAndActiveTrueAndTenant(String targetModule, String tenant) {
        return ruleSetJpaRepository.findByTargetModuleAndActiveTrueAndTenant(targetModule, tenant).stream()
                .map(ruleSetMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<RuleSet> findAllByTenant(String tenant) {
        return ruleSetJpaRepository.findAllByTenant(tenant).stream()
                .map(ruleSetMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        ruleSetJpaRepository.deleteById(id);
    }
}
