package com.homebase.ecom.compliance.infrastructure.persistence.repository;

import com.homebase.ecom.compliance.infrastructure.persistence.entity.ComplianceAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplianceAuditJpaRepository extends JpaRepository<ComplianceAuditEntity, String> {
    List<ComplianceAuditEntity> findByRegulationId(String regulationId);
}
