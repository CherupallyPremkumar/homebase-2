package com.homebase.ecom.catalog.domain.port.in;

import com.homebase.ecom.catalog.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    /**
     * Create a new category
     */
    Category createCategory(Category category);

    /**
     * Update an existing category
     */
    Category updateCategory(String id, Category category);

    /**
     * Get category by ID
     */
    Optional<Category> getCategoryById(String id);

    /**
     * Get all root categories (level 0)
     */
    List<Category> getRootCategories();

    /**
     * Get children of a category
     */
    List<Category> getChildCategories(String parentId);

    /**
     * Get full category tree
     */
    List<Category> getCategoryTree();

    /**
     * Delete a category (soft delete - set active=false)
     */
    void deleteCategory(String id);
}
