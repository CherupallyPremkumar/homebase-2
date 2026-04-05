package com.homebase.ecom.supplierlifecycle.domain.port;

import java.util.List;
import java.util.Optional;
import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;

public interface SupplierLifecycleSagaRepository {
    Optional<SupplierLifecycleSaga> findById(String id);
    List<SupplierLifecycleSaga> findBySupplierId(String supplierId);
    void save(SupplierLifecycleSaga saga);
    void delete(String id);
}
