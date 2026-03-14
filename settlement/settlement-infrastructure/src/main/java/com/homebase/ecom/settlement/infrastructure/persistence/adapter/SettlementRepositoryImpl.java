package com.homebase.ecom.settlement.infrastructure.persistence.adapter;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.domain.port.SettlementRepository;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementEntity;
import com.homebase.ecom.settlement.infrastructure.persistence.mapper.SettlementMapper;
import java.util.Optional;

public class SettlementRepositoryImpl implements SettlementRepository {

    private final SettlementJpaRepository settlementJpaRepository;
    private final SettlementMapper settlementMapper;

    public SettlementRepositoryImpl(SettlementJpaRepository settlementJpaRepository, SettlementMapper settlementMapper) {
        this.settlementJpaRepository = settlementJpaRepository;
        this.settlementMapper = settlementMapper;
    }

    @Override
    public Optional<Settlement> findById(String id) {
        return settlementJpaRepository.findById(id).map(settlementMapper::toModel);
    }

    @Override
    public void save(Settlement settlement) {
        SettlementEntity entity = settlementMapper.toEntity(settlement);
        settlementJpaRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        settlementJpaRepository.deleteById(id);
    }
}
