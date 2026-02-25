package com.homebase.ecom.settlement.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.settlement.model.Settlement;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.homebase.ecom.settlement.configuration.dao.SettlementRepository;
import java.util.Optional;

public class SettlementEntityStore implements EntityStore<Settlement>{
    @Autowired private SettlementRepository settlementRepository;

	@Override
	public void store(Settlement entity) {
        settlementRepository.save(entity);
	}

	@Override
	public Settlement retrieve(String id) {
        Optional<Settlement> entity = settlementRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Settlement with ID " + id);
	}

}
