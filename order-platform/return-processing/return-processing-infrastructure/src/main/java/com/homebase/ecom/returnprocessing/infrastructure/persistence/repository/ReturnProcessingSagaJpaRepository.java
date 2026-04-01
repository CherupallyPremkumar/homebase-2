package com.homebase.ecom.returnprocessing.infrastructure.persistence.repository;

import com.homebase.ecom.returnprocessing.infrastructure.persistence.entity.ReturnProcessingSagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnProcessingSagaJpaRepository extends JpaRepository<ReturnProcessingSagaEntity, String> {

    Optional<ReturnProcessingSagaEntity> findByReturnRequestId(String returnRequestId);
}
