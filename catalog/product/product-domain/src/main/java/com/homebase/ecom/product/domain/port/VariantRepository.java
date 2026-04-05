package com.homebase.ecom.product.domain.port;

import com.homebase.ecom.product.domain.model.Variant;
import java.util.List;
import java.util.Optional;

public interface VariantRepository {
    Optional<Variant> findById(String id);
    List<Variant> findByProductId(String productId);
    void save(Variant variant);
    void delete(String id);
}
