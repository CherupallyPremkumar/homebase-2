package com.homebase.ecom.returnprocessing.infrastructure.persistence;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingSagaRepository;
import org.chenile.base.exception.NotFoundException;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ChenileReturnProcessingEntityStore implements EntityStore<ReturnProcessingSaga> {

    @Autowired
    private ReturnProcessingSagaRepository returnProcessingSagaRepository;

    @Override
    public void store(ReturnProcessingSaga entity) {
        returnProcessingSagaRepository.save(entity);
    }

    @Override
    public ReturnProcessingSaga retrieve(String id) {
        Optional<ReturnProcessingSaga> entity = returnProcessingSagaRepository.findById(id);
        if (entity.isPresent())
            return entity.get();
        throw new NotFoundException(1600, "Unable to find ReturnProcessingSaga with ID " + id);
    }
}
