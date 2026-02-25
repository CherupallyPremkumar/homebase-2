package com.homebase.ecom.settlement.configuration.dao;

import com.homebase.ecom.settlement.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface SettlementRepository extends JpaRepository<Settlement,String> {}
