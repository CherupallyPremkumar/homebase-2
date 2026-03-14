package com.homebase.ecom.policy.infrastructure.persistence.adapter;

import com.homebase.ecom.policy.domain.model.FactDefinition;
import com.homebase.ecom.policy.domain.repository.FactDefinitionRepository;
import com.homebase.ecom.policy.infrastructure.persistence.mapper.FactDefinitionMapper;
import java.util.List;
import java.util.stream.Collectors;

public class FactDefinitionRepositoryImpl implements FactDefinitionRepository {

    private final FactDefinitionJpaRepository jpaRepository;
    private final FactDefinitionMapper mapper;

    public FactDefinitionRepositoryImpl(FactDefinitionJpaRepository jpaRepository, FactDefinitionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<FactDefinition> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactDefinition> findByModule(String module) {
        return jpaRepository.findByModule(module).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<FactDefinition> factDefinitions) {
        jpaRepository.saveAll(factDefinitions.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
    }
}
