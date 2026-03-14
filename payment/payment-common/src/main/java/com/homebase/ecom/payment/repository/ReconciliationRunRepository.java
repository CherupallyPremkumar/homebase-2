package com.homebase.ecom.payment.repository;

import com.homebase.ecom.payment.domain.ReconciliationRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReconciliationRunRepository extends JpaRepository<ReconciliationRun, String> {

    List<ReconciliationRun> findByGatewayTypeOrderByPeriodStartDesc(String gatewayType);
}
