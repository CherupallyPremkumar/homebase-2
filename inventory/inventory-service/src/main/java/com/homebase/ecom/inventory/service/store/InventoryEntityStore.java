package com.homebase.ecom.inventory.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.inventory.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.homebase.ecom.inventory.configuration.dao.InventoryRepository;
import java.util.Optional;

public class InventoryEntityStore implements EntityStore<Inventory>{
    @Autowired private InventoryRepository inventoryRepository;

	@Override
	public void store(Inventory entity) {
        inventoryRepository.save(entity);
	}

	@Override
	public Inventory retrieve(String id) {
        Optional<Inventory> entity = inventoryRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Inventory with ID " + id);
	}

}
