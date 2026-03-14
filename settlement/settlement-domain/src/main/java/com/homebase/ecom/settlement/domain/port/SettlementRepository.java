package com.homebase.ecom.settlement.domain.port;

import com.homebase.ecom.settlement.model.Settlement;

import java.util.Optional;

public interface SettlementRepository {

    Optional<Settlement> findById(String id);

    void save(Settlement settlement);

    void delete(String id);
}
