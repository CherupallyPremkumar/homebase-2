package com.homebase.ecom.compliance.infrastructure.persistence.repository;

import com.homebase.ecom.compliance.infrastructure.persistence.entity.RegulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegulationJpaRepository extends JpaRepository<RegulationEntity, String> {
    List<RegulationEntity> findByJurisdiction(String jurisdiction);
    List<RegulationEntity> findByActiveTrue();
}
