package com.homebase.ecom.returnprocessing.port;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;

import java.util.Optional;

/**
 * Port (domain interface) for persisting and retrieving return processing sagas.
 */
public interface ReturnProcessingSagaRepository {

    Optional<ReturnProcessingSaga> findById(String id);

    Optional<ReturnProcessingSaga> findByReturnRequestId(String returnRequestId);

    ReturnProcessingSaga save(ReturnProcessingSaga saga);

    void delete(String id);
}
