package com.homebase.ecom.policy.infrastructure.persistence.repository;

import com.homebase.ecom.policy.infrastructure.persistence.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PolicyJpaRepository extends JpaRepository<PolicyEntity, String> {
    List<PolicyEntity> findByTargetModuleAndActiveTrue(String targetModule);
}
