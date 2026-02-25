package com.homebase.ecom.supplierproduct.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.supplierproduct.model.Supplierproduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.homebase.ecom.supplierproduct.configuration.dao.SupplierproductRepository;
import java.util.Optional;

public class SupplierproductEntityStore implements EntityStore<Supplierproduct>{
    @Autowired private SupplierproductRepository supplierproductRepository;

	@Override
	public void store(Supplierproduct entity) {
        supplierproductRepository.save(entity);
	}

	@Override
	public Supplierproduct retrieve(String id) {
        Optional<Supplierproduct> entity = supplierproductRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Supplierproduct with ID " + id);
	}

}
