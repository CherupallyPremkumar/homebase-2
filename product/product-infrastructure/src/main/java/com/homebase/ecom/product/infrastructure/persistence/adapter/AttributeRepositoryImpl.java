package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.domain.model.AttributeDefinition;
import com.homebase.ecom.product.domain.port.AttributeRepository;
import com.homebase.ecom.product.infrastructure.persistence.entity.AttributeDefinitionEntity;
import com.homebase.ecom.product.infrastructure.persistence.mapper.AttributeMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AttributeRepositoryImpl implements AttributeRepository {

    private final AttributeJpaRepository jpaRepository;
    private final AttributeMapper mapper;

    public AttributeRepositoryImpl(AttributeJpaRepository jpaRepository, AttributeMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<AttributeDefinition> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<AttributeDefinition> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toModel);
    }

    @Override
    public List<AttributeDefinition> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttributeDefinition> findAllById(List<String> ids) {
        return jpaRepository.findAllById(ids).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeDefinition save(AttributeDefinition attribute) {
        AttributeDefinitionEntity saved = jpaRepository.save(mapper.toEntity(attribute));
        return mapper.toModel(saved);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    public interface AttributeJpaRepository extends JpaRepository<AttributeDefinitionEntity, String> {
        Optional<AttributeDefinitionEntity> findByCode(String code);
    }
}
