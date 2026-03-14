package com.homebase.ecom.product.api;

import com.homebase.ecom.product.dto.AttributeDefinitionDto;
import com.homebase.ecom.product.dto.CategoryDto;

import java.util.List;

/**
 * Service interface for managing PIM Taxonomy (Categories and Attributes).
 */
public interface TaxonomyService {

    // Category Management
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(String id, CategoryDto categoryDto);
    CategoryDto getCategory(String id);
    List<CategoryDto> getSubCategories(String parentId);
    List<CategoryDto> getCategoryTree();
    void deleteCategory(String id);

    // Attribute Management
    AttributeDefinitionDto createAttribute(AttributeDefinitionDto attributeDto);
    AttributeDefinitionDto updateAttribute(String id, AttributeDefinitionDto attributeDto);
    AttributeDefinitionDto getAttribute(String id);
    List<AttributeDefinitionDto> getAttributesByCategory(String categoryId);
    void deleteAttribute(String id);
}
