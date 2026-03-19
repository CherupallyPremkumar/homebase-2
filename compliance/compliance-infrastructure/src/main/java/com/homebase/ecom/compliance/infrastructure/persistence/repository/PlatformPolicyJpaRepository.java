package com.homebase.ecom.compliance.infrastructure.persistence.repository;

import com.homebase.ecom.compliance.infrastructure.persistence.entity.PlatformPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlatformPolicyJpaRepository extends JpaRepository<PlatformPolicyEntity, String> {
    List<PlatformPolicyEntity> findByPolicyCategory(String policyCategory);

    @Query("SELECT p FROM PlatformPolicyEntity p WHERE p.policyCategory = :category AND p.state.stateId = :stateId")
    List<PlatformPolicyEntity> findByPolicyCategoryAndStateId(@Param("category") String policyCategory, @Param("stateId") String stateId);
}
