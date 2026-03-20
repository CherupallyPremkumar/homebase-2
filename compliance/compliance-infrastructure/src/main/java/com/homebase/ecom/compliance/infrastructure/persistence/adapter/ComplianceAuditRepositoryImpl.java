package com.homebase.ecom.compliance.infrastructure.persistence.adapter;

import com.homebase.ecom.compliance.model.ComplianceAudit;
import com.homebase.ecom.compliance.port.out.ComplianceAuditRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.ComplianceAuditEntity;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.ComplianceAuditJpaRepository;

import java.util.List;

public class ComplianceAuditRepositoryImpl implements ComplianceAuditRepository {

    private final ComplianceAuditJpaRepository jpaRepo;

    public ComplianceAuditRepositoryImpl(ComplianceAuditJpaRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public ComplianceAudit save(ComplianceAudit audit) {
        return toModel(jpaRepo.save(toEntity(audit)));
    }

    @Override
    public List<ComplianceAudit> findByRegulationId(String regulationId) {
        return jpaRepo.findByRegulationId(regulationId).stream().map(this::toModel).toList();
    }

    private ComplianceAudit toModel(ComplianceAuditEntity entity) {
        ComplianceAudit model = new ComplianceAudit();
        model.setId(entity.getId());
        model.setRegulationId(entity.getRegulationId());
        model.setAuditType(entity.getAuditType());
        model.setStatus(entity.getStatus());
        model.setFindings(entity.getFindings());
        model.setAuditorId(entity.getAuditorId());
        model.setAuditDate(entity.getAuditDate());
        model.setNextAuditDate(entity.getNextAuditDate());
        model.setTenant(entity.tenant);
        return model;
    }

    private ComplianceAuditEntity toEntity(ComplianceAudit model) {
        ComplianceAuditEntity entity = new ComplianceAuditEntity();
        entity.setId(model.getId());
        entity.setRegulationId(model.getRegulationId());
        entity.setAuditType(model.getAuditType());
        entity.setStatus(model.getStatus());
        entity.setFindings(model.getFindings());
        entity.setAuditorId(model.getAuditorId());
        entity.setAuditDate(model.getAuditDate());
        entity.setNextAuditDate(model.getNextAuditDate());
        entity.tenant = model.getTenant();
        return entity;
    }
}
