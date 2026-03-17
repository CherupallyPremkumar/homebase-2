package com.homebase.ecom.rulesengine.infrastructure.persistence.adapter;

import com.homebase.ecom.rulesengine.domain.model.Decision;
import com.homebase.ecom.rulesengine.domain.repository.DecisionRepository;
import com.homebase.ecom.rulesengine.infrastructure.persistence.entity.DecisionEntity;
import com.homebase.ecom.rulesengine.infrastructure.persistence.mapper.DecisionMapper;
import com.homebase.ecom.rulesengine.infrastructure.persistence.repository.DecisionJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DecisionRepositoryImpl implements DecisionRepository {

    private final DecisionJpaRepository decisionJpaRepository;
    private final DecisionMapper decisionMapper;

    public DecisionRepositoryImpl(DecisionJpaRepository decisionJpaRepository, DecisionMapper decisionMapper) {
        this.decisionJpaRepository = decisionJpaRepository;
        this.decisionMapper = decisionMapper;
    }

    @Override
    public Decision save(Decision decision) {
        DecisionEntity entity = decisionMapper.toEntity(decision);
        DecisionEntity saved = decisionJpaRepository.save(entity);
        return decisionMapper.toModel(saved);
    }

    @Override
    public Optional<Decision> findById(String id) {
        return decisionJpaRepository.findById(id)
                .map(decisionMapper::toModel);
    }

    @Override
    public List<Decision> findAll() {
        return decisionJpaRepository.findAll().stream()
                .map(decisionMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Decision> findByRuleSetId(String ruleSetId) {
        return decisionJpaRepository.findByRuleSetId(ruleSetId).stream()
                .map(decisionMapper::toModel)
                .collect(Collectors.toList());
    }
}
