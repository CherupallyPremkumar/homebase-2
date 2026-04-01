package com.homebase.ecom.compliance.infrastructure.persistence.repository;

import com.homebase.ecom.compliance.infrastructure.persistence.entity.AgreementAcceptanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgreementAcceptanceJpaRepository extends JpaRepository<AgreementAcceptanceEntity, String> {
    Optional<AgreementAcceptanceEntity> findByUserIdAndAgreementId(String userId, String agreementId);
    List<AgreementAcceptanceEntity> findByAgreementId(String agreementId);
    List<AgreementAcceptanceEntity> findByUserId(String userId);
}
