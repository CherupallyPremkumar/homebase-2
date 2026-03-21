package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.ReconciliationRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReconciliationRunRepository extends JpaRepository<ReconciliationRun, String> {

    List<ReconciliationRun> findByGatewayTypeOrderByPeriodStartDesc(String gatewayType);
}
