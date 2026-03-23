package com.homebase.ecom.settlement.infrastructure.persistence;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementEntity;
import com.homebase.ecom.settlement.infrastructure.persistence.adapter.SettlementJpaRepository;
import com.homebase.ecom.settlement.infrastructure.persistence.mapper.SettlementMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;

public class ChenileSettlementEntityStore extends ChenileJpaEntityStore<Settlement, SettlementEntity> {

    public ChenileSettlementEntityStore(SettlementJpaRepository repository, SettlementMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
