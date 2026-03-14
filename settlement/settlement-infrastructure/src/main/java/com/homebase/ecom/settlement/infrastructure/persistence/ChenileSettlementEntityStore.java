package com.homebase.ecom.settlement.infrastructure.persistence;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.domain.port.SettlementRepository;
import com.homebase.ecom.settlement.infrastructure.persistence.adapter.SettlementJpaRepository;
import com.homebase.ecom.settlement.infrastructure.persistence.adapter.SettlementRepositoryImpl;
import com.homebase.ecom.settlement.infrastructure.persistence.mapper.SettlementMapper;
import org.chenile.base.exception.NotFoundException;
import org.chenile.utils.entity.service.EntityStore;

import java.util.Optional;

public class ChenileSettlementEntityStore implements EntityStore<Settlement> {

    private final SettlementRepository settlementRepository;

    public ChenileSettlementEntityStore(SettlementJpaRepository jpaRepository, SettlementMapper mapper) {
        this.settlementRepository = new SettlementRepositoryImpl(jpaRepository, mapper);
    }

    @Override
    public void store(Settlement entity) {
        settlementRepository.save(entity);
    }

    @Override
    public Settlement retrieve(String id) {
        Optional<Settlement> entity = settlementRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500, "Unable to find Settlement with ID " + id);
    }
}
