package com.homebase.ecom.catalog.repository;

import com.homebase.ecom.catalog.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(String id);
    List<Category> findRootCategories();
    List<Category> findByParentId(String parentId);
    List<Category> findByActiveTrue();
    Category save(Category category);
    long count();
    void deleteById(String id);
}
