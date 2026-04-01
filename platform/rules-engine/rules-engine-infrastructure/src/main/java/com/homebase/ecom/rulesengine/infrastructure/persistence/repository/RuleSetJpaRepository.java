package com.homebase.ecom.rulesengine.infrastructure.persistence.repository;

import com.homebase.ecom.rulesengine.infrastructure.persistence.entity.RuleSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RuleSetJpaRepository extends JpaRepository<RuleSetEntity, String> {
    @Query("SELECT DISTINCT rs FROM RuleSetEntity rs LEFT JOIN FETCH rs.rules WHERE rs.targetModule = :targetModule AND rs.active = true")
    List<RuleSetEntity> findByTargetModuleAndActiveTrue(@Param("targetModule") String targetModule);

    @Query("SELECT DISTINCT rs FROM RuleSetEntity rs LEFT JOIN FETCH rs.rules WHERE rs.id = :id")
    Optional<RuleSetEntity> findByIdWithRules(@Param("id") String id);

    @Query("SELECT DISTINCT rs FROM RuleSetEntity rs LEFT JOIN FETCH rs.rules")
    List<RuleSetEntity> findAllWithRules();

    @Query("SELECT DISTINCT rs FROM RuleSetEntity rs LEFT JOIN FETCH rs.rules WHERE rs.targetModule = :targetModule AND rs.active = true AND rs.tenant = :tenant")
    List<RuleSetEntity> findByTargetModuleAndActiveTrueAndTenant(@Param("targetModule") String targetModule, @Param("tenant") String tenant);

    @Query("SELECT DISTINCT rs FROM RuleSetEntity rs LEFT JOIN FETCH rs.rules WHERE rs.tenant = :tenant")
    List<RuleSetEntity> findAllByTenant(@Param("tenant") String tenant);
}
