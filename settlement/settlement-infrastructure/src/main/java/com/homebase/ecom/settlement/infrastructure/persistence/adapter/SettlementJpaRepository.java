package com.homebase.ecom.settlement.infrastructure.persistence.adapter;

import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementJpaRepository extends JpaRepository<SettlementEntity, String> {
}
