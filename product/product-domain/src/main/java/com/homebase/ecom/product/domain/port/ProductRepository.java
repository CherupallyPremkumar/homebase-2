package com.homebase.ecom.product.domain.port;

import com.homebase.ecom.product.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(String id);
    void save(Product product);
    void delete(String id);
    List<Product> findBySupplierIdAndStateId(String supplierId, String stateId);
    List<Product> findBySupplierId(String supplierId);
}
