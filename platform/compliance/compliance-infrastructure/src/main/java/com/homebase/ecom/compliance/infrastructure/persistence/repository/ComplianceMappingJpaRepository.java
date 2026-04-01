package com.homebase.ecom.compliance.infrastructure.persistence.repository;

import com.homebase.ecom.compliance.infrastructure.persistence.entity.ComplianceMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplianceMappingJpaRepository extends JpaRepository<ComplianceMappingEntity, String> {
    List<ComplianceMappingEntity> findByRegulationId(String regulationId);
    List<ComplianceMappingEntity> findByTargetId(String targetId);
}
