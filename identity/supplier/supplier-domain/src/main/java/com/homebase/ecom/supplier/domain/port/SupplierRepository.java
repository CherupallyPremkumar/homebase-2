package com.homebase.ecom.supplier.domain.port;

import com.homebase.ecom.supplier.model.Supplier;

import java.util.Optional;

/**
 * Outbound Port (Hexagonal): Supplier persistence.
 *
 * Domain depends on this interface. JPA implementation lives in supplier-infrastructure.
 * No Spring/JPA annotations here.
 */
public interface SupplierRepository {

    Optional<Supplier> findById(String id);

    void save(Supplier supplier);

    void delete(String id);
}
