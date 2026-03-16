package com.homebase.ecom.settlement.domain.port;

import com.homebase.ecom.settlement.model.Settlement;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for settlement persistence.
 */
public interface SettlementRepository {

    Optional<Settlement> findById(String id);

    void save(Settlement settlement);

    void delete(String id);

    List<Settlement> findBySupplierId(String supplierId);

    Optional<Settlement> findByOrderId(String orderId);
}
