package com.homebase.ecom.compliance.infrastructure.persistence.adapter;

import com.homebase.ecom.compliance.model.ComplianceMapping;
import com.homebase.ecom.compliance.port.out.ComplianceMappingRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.ComplianceMappingEntity;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.ComplianceMappingJpaRepository;

import java.util.List;

public class ComplianceMappingRepositoryImpl implements ComplianceMappingRepository {

    private final ComplianceMappingJpaRepository jpaRepo;

    public ComplianceMappingRepositoryImpl(ComplianceMappingJpaRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public ComplianceMapping save(ComplianceMapping mapping) {
        return toModel(jpaRepo.save(toEntity(mapping)));
    }

    @Override
    public List<ComplianceMapping> findByRegulationId(String regulationId) {
        return jpaRepo.findByRegulationId(regulationId).stream().map(this::toModel).toList();
    }

    @Override
    public List<ComplianceMapping> findByTargetId(String targetId) {
        return jpaRepo.findByTargetId(targetId).stream().map(this::toModel).toList();
    }

    private ComplianceMapping toModel(ComplianceMappingEntity entity) {
        ComplianceMapping model = new ComplianceMapping();
        model.setId(entity.getId());
        model.setRegulationId(entity.getRegulationId());
        model.setMappingType(entity.getMappingType());
        model.setTargetId(entity.getTargetId());
        model.setDescription(entity.getDescription());
        model.setTenant(entity.tenant);
        return model;
    }

    private ComplianceMappingEntity toEntity(ComplianceMapping model) {
        ComplianceMappingEntity entity = new ComplianceMappingEntity();
        entity.setId(model.getId());
        entity.setRegulationId(model.getRegulationId());
        entity.setMappingType(model.getMappingType());
        entity.setTargetId(model.getTargetId());
        entity.setDescription(model.getDescription());
        entity.tenant = model.getTenant();
        return entity;
    }
}
