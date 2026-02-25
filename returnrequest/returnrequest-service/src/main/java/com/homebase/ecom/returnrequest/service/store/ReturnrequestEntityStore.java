package com.homebase.ecom.returnrequest.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.homebase.ecom.returnrequest.configuration.dao.ReturnrequestRepository;
import java.util.Optional;

public class ReturnrequestEntityStore implements EntityStore<Returnrequest>{
    @Autowired private ReturnrequestRepository returnrequestRepository;

	@Override
	public void store(Returnrequest entity) {
        returnrequestRepository.save(entity);
	}

	@Override
	public Returnrequest retrieve(String id) {
        Optional<Returnrequest> entity = returnrequestRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Returnrequest with ID " + id);
	}

}
