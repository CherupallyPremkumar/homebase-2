package com.homebase.ecom.settlement.infrastructure.persistence.adapter;

import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettlementJpaRepository extends JpaRepository<SettlementEntity, String> {

    List<SettlementEntity> findBySupplierId(String supplierId);

    Optional<SettlementEntity> findByOrderId(String orderId);
}
