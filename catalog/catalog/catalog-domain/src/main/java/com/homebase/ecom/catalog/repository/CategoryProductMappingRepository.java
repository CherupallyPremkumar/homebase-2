package com.homebase.ecom.catalog.repository;

import com.homebase.ecom.catalog.model.CategoryProductMapping;
import java.util.List;
import java.util.Optional;

public interface CategoryProductMappingRepository {
    List<CategoryProductMapping> findByCategoryIdOrderByDisplayOrderAsc(String categoryId);
    Optional<CategoryProductMapping> findByCategoryIdAndProductId(String categoryId, String productId);
    List<CategoryProductMapping> findByProductId(String productId);
    CategoryProductMapping save(CategoryProductMapping mapping);
    void deleteByCategoryIdAndProductId(String categoryId, String productId);
}
