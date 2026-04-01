package com.homebase.ecom.compliance.infrastructure.persistence.repository;

import com.homebase.ecom.compliance.infrastructure.persistence.entity.AgreementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgreementJpaRepository extends JpaRepository<AgreementEntity, String> {
    List<AgreementEntity> findByAgreementType(String agreementType);

    @Query("SELECT a FROM AgreementEntity a WHERE a.agreementType = :type AND a.state.stateId = :stateId")
    List<AgreementEntity> findByAgreementTypeAndStateId(@Param("type") String agreementType, @Param("stateId") String stateId);
}
