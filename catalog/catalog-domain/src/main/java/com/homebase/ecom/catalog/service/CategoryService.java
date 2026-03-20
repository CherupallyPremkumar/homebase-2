package com.homebase.ecom.catalog.service;

import com.homebase.ecom.catalog.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category createCategory(Category category);
    Category updateCategory(String id, Category category);
    Optional<Category> getCategoryById(String id);
    List<Category> getRootCategories();
    List<Category> getChildCategories(String parentId);
    List<Category> getCategoryTree();
    void deleteCategory(String id);
}
