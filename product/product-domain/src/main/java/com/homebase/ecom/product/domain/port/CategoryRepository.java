package com.homebase.ecom.product.domain.port;

import com.homebase.ecom.product.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(String id);
    Optional<Category> findBySlug(String slug);
    List<Category> findByParentId(String parentId);
    List<Category> findAll();
    List<Category> findAllActive();
    Category save(Category category);
    void delete(String id);
    void deleteById(String id);
}
